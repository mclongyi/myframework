package com.odianyun.search.whale.resp.handler;

import com.odianyun.search.whale.analysis.PinYin;
import com.odianyun.search.whale.api.model.ZeroResultRecommendResult;
import com.odianyun.search.whale.api.model.req.SearchRequest;
import com.odianyun.search.whale.common.IndexNameManager;
import com.odianyun.search.whale.common.query.ChineseToHanYuPY;
import com.odianyun.search.whale.common.query.KeywordSearchHelper;
import com.odianyun.search.whale.es.api.ESService;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.manager.IKSegmentManager;
import com.odianyun.search.whale.req.builder.RequestBuilder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//@Component
public class ZeroResultHandler {


	static Logger logger = Logger.getLogger(ZeroResultHandler.class);
	@Autowired
	List<RequestBuilder> requestBuilders;
	@Autowired
	List<ResponseHandler<?>> responseHandlers;
	@Autowired
	List<RequestBuilder> lessRequestBuilders;
	@Autowired
	List<ResponseHandler> lessResponseHandlers;
	@Autowired
	EditDistance editDistance;

	//搜索词不大于某个数
	public static final int maxSeachWord = 15;
	public static final int lessResultSize = 0; //如果查询的结果数大于此阈值 则不进行零少结果的处理
//	int tokensMaxSize=5; //分词后的词语数超过此阈值，则不进行零少结果的逻辑
	int maxEnglishTokenLength=12; //进行纠错处理时用户的拼音长度必须有个规定，以防过长 使查询开销变得太大
	int minEnglishTokenLength=3; //零少结果中 如果词语全为拼音，则拼音的最小长度要有规定 以防用户随机输入几个字母
	int maxMachTokens=3; //零少结果中，词语组合的search中 词语的个数必须小于的值
	int minMachTokens=1;  //零少结果中，进入纠错的条件 及词语个数为1
	int mayBeInterestedKeyWordsLimit=5;//可能感兴趣的词最多6个
	int minDuplicateRemovalLength=1; //分词后的list 进行去重时，排除掉的词的最小长度
	int minCombineSourceDataSize=1;//集合 的排列组合中 源列表的最小长度
	int maxCombineSourceDataSize=3; //集合 的排列组合中 源列表的最大长度
	//英文正则表达式
	String englishRegExp="[a-zA-Z]+";

	/**
	 * 不满足零结果处理的条件
	 * 1，总结果数大于0
	 * 2，关键词为空
	 * 3，关键词分词的个数大于3
	 *
	 * 处理逻辑：首先进行分词
	 * 1.如果词数=1
	 *  1.1 则判断是否全是字符，如果是 则进行拼音纠错处理，找出与目标词最相近的汉字拼音 并把对应汉字设置为searchRequest 的keyword
	 *  1.2 否则 其为汉字 ，则有可能拼音输入法时，拼音输入正确但是汉字选择错误，所以此时进行拼音转换，对其拼音进行搜索
	 * 2.否则默认词数为 小于4 大于0  则采取 math.ceil(tokens.size()/2) should方式build
	 * 3.添加可能感兴趣的词；这些词是由输入的长句直接切词或者纠错后词语生成的；
	 * 		这里直接添加进入可能感兴趣的词；如果词数超过5个 如果一个个查找，需要查找5次，才能确认是否有结果，所以代价太高；
	 * 		如果放在零少逻辑里面 ，如果为一个词，则可以直接加入 加不加都无意义；如果为2-3个词，放到 组合中是否可以找到，来添加他们组合中的词，则3个词的组合中有可能会漏一个 所以这里暂时先这样，后续再想对策
	 */
	public void handleBack(SearchResponse esSearchResponse,
					   com.odianyun.search.whale.api.model.SearchResponse searchResponse,
                       SearchRequest searchRequest) throws Exception {

		if(!searchRequest.isZeroResponseHandler()){
			return;
		}
		String keyword=searchRequest.getKeyword();
		if(esSearchResponse.getHits().getTotalHits()>lessResultSize
				|| StringUtils.isBlank(keyword)){
			return;
		}
		//1.先转换为拼音搜索来搜 鱼蛋--》yudan  yd
		chinesePinYinOrEnglishReSearch(searchRequest,searchResponse);
		if(searchResponse.getTotalHit()>0){
			return;
		}

		//2.拼音搜索不到，分词，推荐
		List<String> tokens = IKSegmentManager.segment(keyword);
		//分词之后是一个词,那就不需要推荐
		if(CollectionUtils.isEmpty(tokens) || tokens.size()==1){
			return;
		}
		List<String> tokenList=duplicateRemoval(tokens);
		//在这里直接加入你可能感兴趣的词语，；如果分词个数大于tokensMaxSize  下面直接回return 所以选择先在此直接加入，如果进入零少结果，则会按照零少结果的方式处理
		if (CollectionUtils.isEmpty(tokenList) ) {
			return;
		}
		int tokenSize=tokenList.size();
		if(tokenList.size()==1){
			searchRequest.setKeyword(tokenList.get(0));
			reSearch(searchRequest,searchResponse);
			return;
		}
		//暂时只处理关键词分词的小于等于3的情况
		if(tokenSize>maxMachTokens && tokenSize<=mayBeInterestedKeyWordsLimit) {
			searchResponse.getZeroResultRecommendResult().setMayBeInterestedKeyWords(filterNoResultWords(tokenList,searchRequest));
			return;
		}

		//此处代码为 部分匹配查询
		/**
		 * 此处代码为 部分匹配查询
		 * 1.满足条件，分词数为 大于1个 小于等于3个
		 * 步骤1.首先对词语进行满足条件的组合 eg：3个词 中的两个词的组合 有3种；
		 * 步骤2.根据这三种进行分别进行查询，最终零少结果字段中放置的应该是 查询结果数目组多的那个结果
		 */
		if(tokenSize>minMachTokens && tokenSize<= maxMachTokens) {
			long maxmerchantProductResultSize=0L;
			ZeroResultRecommendResult zeroResultRecommendResultList = new ZeroResultRecommendResult();
			SearchResponse partEsSearchResponse = new SearchResponse();
			List<String> combineWord = combine(tokenList, " ");//此处对关键词list 进行排列组合 此函数 只适用于（2个，3个词）的情况
			try {

				for (String oneWord : combineWord) {
                    ESSearchRequest esSearchRequest=new ESSearchRequest(IndexNameManager.getIndexName(searchRequest), IndexConstants.index_type);
					searchRequest.setKeyword(oneWord);
					if (lessRequestBuilders != null) {// construct elastic request parameter
						for (RequestBuilder builder : lessRequestBuilders) {
							builder.build(esSearchRequest, searchRequest);
						}
					}
					partEsSearchResponse = ESService.search(esSearchRequest);
					long resultSize=partEsSearchResponse.getHits().getTotalHits();
					if (responseHandlers != null && maxmerchantProductResultSize<resultSize) {// 处理es返回结果并转成业务结果集
						for (ResponseHandler responseHandler : lessResponseHandlers) {
							responseHandler.handle(partEsSearchResponse, searchResponse, esSearchRequest, searchRequest);
						}
						maxmerchantProductResultSize=resultSize;
						zeroResultRecommendResultList=new ZeroResultRecommendResult(oneWord,searchResponse.getZeroResultRecommendResult().getMerchantProducts());
					}
				}
				searchResponse.setZeroResultRecommendResult(zeroResultRecommendResultList);
				if(tokenSize==2) {
					tokenList.remove(zeroResultRecommendResultList.getRecommendWord());
					searchResponse.getZeroResultRecommendResult().setMayBeInterestedKeyWords(filterNoResultWords(tokenList,searchRequest));
				}else {
					searchResponse.getZeroResultRecommendResult().setMayBeInterestedKeyWords(filterNoResultWords(tokenList,searchRequest));
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}


	public void handle(SearchResponse esSearchResponse,
					   com.odianyun.search.whale.api.model.SearchResponse searchResponse,
					   SearchRequest searchRequest) throws Exception {
		if(!searchRequest.isZeroResponseHandler()){
			return;
		}
		String keyword=searchRequest.getKeyword();
		if(esSearchResponse.getHits().getTotalHits()>lessResultSize
				|| StringUtils.isBlank(keyword)){
			return;
		}
		//鱼蛋-->鱼蛋，鱼，蛋，yd,yudan
		List<String> tokens = new ArrayList<>();
		try {
			List<String> py = PinYin.transZHToPinyin(keyword, true, true);
			List<String> zhs = IKSegmentManager.segment(keyword,false);
			tokens.addAll(py);
			tokens.addAll(zhs);
		}catch (Exception e){
			logger.error("分词失败",e);
		}
		logger.info("针对零结果处理重新搜索："+tokens);
		if(!tokens.isEmpty()){
			if (tokens.size()>maxSeachWord){
                searchRequest.setTokens(tokens.subList(0,maxSeachWord-1));
            }else {
                searchRequest.setTokens(tokens);
            }
            reSearchResponseNormal(searchRequest,searchResponse);
		}
	}

	private void chinesePinYinOrEnglishReSearch(SearchRequest searchRequest,com.odianyun.search.whale.api.model.SearchResponse searchResponse){
		String newSearchWord = calcNewSearchWord(searchRequest.getKeyword(), searchRequest.getCompanyId());
		if(StringUtils.isNotBlank(newSearchWord) && !newSearchWord.equals(searchRequest.getKeyword())) {
			searchRequest.setKeyword(newSearchWord);
			try {
				reSearch(searchRequest,searchResponse);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	private void reSearch(SearchRequest searchRequest,com.odianyun.search.whale.api.model.SearchResponse searchResponse) throws Exception {
		ESSearchRequest esSearchRequest=new ESSearchRequest(IndexNameManager.getIndexName(searchRequest), IndexConstants.index_type);
		if (lessRequestBuilders != null) {//construct elastic request parameter
			for (RequestBuilder builder : lessRequestBuilders) {
				builder.build(esSearchRequest, searchRequest); //从此进入 开始分词 KeywordQueryBuilder
			}
		}
		SearchResponse lessEsSearchResponse = ESService.search(esSearchRequest);
		for (ResponseHandler responseHandler : lessResponseHandlers) { //此处handle 只进行了零少结果处理，存入ZeroResultRecommendationProductResult key为纠正后的词 value为 结果list
			responseHandler.handle(lessEsSearchResponse, searchResponse, esSearchRequest, searchRequest);
		}
	}

    private void reSearchResponseNormal(SearchRequest searchRequest,com.odianyun.search.whale.api.model.SearchResponse searchResponse) throws Exception {
        ESSearchRequest esSearchRequest=new ESSearchRequest(IndexNameManager.getIndexName(searchRequest), IndexConstants.index_type);
        if (lessRequestBuilders != null) {//construct elastic request parameter
            for (RequestBuilder builder : lessRequestBuilders) {
                builder.build(esSearchRequest, searchRequest); //从此进入 开始分词 KeywordQueryBuilder
            }
        }
        SearchResponse lessEsSearchResponse = ESService.search(esSearchRequest);
        for (ResponseHandler responseHandler : responseHandlers) {
            responseHandler.handle(lessEsSearchResponse, searchResponse, esSearchRequest, searchRequest);
        }
    }

	private static List<String> filterNoResultWords(List<String> words,SearchRequest searchRequest){
		List<String> hasResultWords=new ArrayList<>();
		if(CollectionUtils.isNotEmpty(words)){
			for(String word:words){
				try{
					long count=KeywordSearchHelper.count(word,searchRequest.getCompanyId());
                     if(count>0){
						 hasResultWords.add(word);
					 }
				}catch(Exception e){

				}
			}
		}
		return hasResultWords;
	}

	private String calcNewSearchWord(String token, int companyId) {
        if(token.matches(englishRegExp)){
            //如果是拼音 结果数为0 证明拼音有可能输错，所以需要纠错
            if(token.length()>minEnglishTokenLength && token.length()<=maxEnglishTokenLength) {
                try {
                    String matchChinese = editDistance.editDistance(token,companyId);
                    if (StringUtils.isNotBlank(matchChinese)) {
                        return matchChinese;
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }else{ //否则为单个ci 则有可能汉字选错 但是拼音是正确的 所以经过拼音转换 得到拼音 进行搜索

            String token_pinyin= ChineseToHanYuPY.instance.convertChineseToPinyin(token);
			//先把这个东西注释掉 2017-02-13
           /** String hanzi = editDistance.getChineseByPinyin(token_pinyin,companyId);
            if (hanzi !=null && !token.equals(hanzi)) { //如果没有找到对应的汉字则不执行
                return hanzi;
            }
			*/
		    return token_pinyin;
        }

        return null;
    }

	public List<ResponseHandler> getLessResponseHandlers() {
		return lessResponseHandlers;
	}

	public void setLessResponseHandlers(List<ResponseHandler> lessResponseHandlers) {
		this.lessResponseHandlers = lessResponseHandlers;
	}

	public List<RequestBuilder> getLessRequestBuilders() {
		return lessRequestBuilders;
	}

	public void setLessRequestBuilders(List<RequestBuilder> lessRequestBuilders) {
		this.lessRequestBuilders = lessRequestBuilders;
	}
	public List<RequestBuilder> getRequestBuilders() {
		return requestBuilders;
	}

	public void setRequestBuilders(List<RequestBuilder> requestBuilders) {
		this.requestBuilders = requestBuilders;
	}

	public List<ResponseHandler<?>> getResponseHandlers() {
		return responseHandlers;
	}

	public void setResponseHandlers(List<ResponseHandler<?>> responseHandlers) {
		this.responseHandlers = responseHandlers;
	}
	//此处是为了排除分词的集合中 词语长度比较小的词语
	private  List<String> duplicateRemoval(List<String> datas) {
		Set<String> ret=new HashSet<>();
		if(datas!=null) {
			for (int i=0;i<datas.size();i++) {//变成集合 去除重复值
				String str=datas.get(i);
				if (str.length() > minDuplicateRemovalLength) {
					ret.add(str);
				}
			}
		}
		return new ArrayList<>(ret);
	}

	private  List<String> combine( List<String> sourceData,String separator) {
		if (sourceData.size()>minCombineSourceDataSize && sourceData.size()<=maxCombineSourceDataSize) {
			List<String> iL1 = new ArrayList<String>();
			for (int i = 0; i < sourceData.size(); i++) {
				List<String> tmpList = new ArrayList<String>();
				tmpList.addAll(sourceData);
				tmpList.remove(i);
				iL1.add(StringUtils.join(tmpList, separator));
			}
			return iL1;
		}
		return null;
	}
}
