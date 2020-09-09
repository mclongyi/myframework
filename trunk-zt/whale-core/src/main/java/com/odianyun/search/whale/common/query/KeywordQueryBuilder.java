package com.odianyun.search.whale.common.query;

import java.util.List;

import com.odianyun.search.whale.api.model.req.BaseSearchRequest;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.ConstantScoreQueryBuilder;
import org.elasticsearch.index.query.DisMaxQueryBuilder;
import org.elasticsearch.index.query.FuzzyQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;

import com.odianyun.search.whale.index.api.common.IndexFieldConstants;
import com.odianyun.search.whale.manager.IKSegmentManager;

public class KeywordQueryBuilder {
	
	static Logger logger = Logger.getLogger(KeywordQueryBuilder.class);
	
	static Fuzziness keywordFuzziness=Fuzziness.fromSimilarity(0.7f);

	public static QueryBuilder buildKeywordQuery(String keyword){
		if(StringUtils.isNotBlank(keyword)){	
			try{
				List<String> tokens = IKSegmentManager.segment(keyword);
				if(CollectionUtils.isNotEmpty(tokens)){
					BoolQueryBuilder keywordQueryBuilder=new BoolQueryBuilder();
					for(String token:tokens){
						/*BoolQueryBuilder tokenQueryBuilder=new BoolQueryBuilder();
						tokenQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.TAG_WORDS,token));
						tokenQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.CATEGORYNAME_SEARCH,token).boost(100.0f));
						tokenQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.BRANDNAME_SEARCH,token).boost(200.0f));
						tokenQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.ATTRVALUE_SEARCH,token));
						tokenQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.MERCHANTNAME_SEARCH,token));
						keywordQueryBuilder.must(tokenQueryBuilder);*/
						DisMaxQueryBuilder disMaxQueryBuilder = new DisMaxQueryBuilder()
										.add(new ConstantScoreQueryBuilder(new TermQueryBuilder(IndexFieldConstants.TAG_WORDS,token))).boost(100.0f)
										.add(new ConstantScoreQueryBuilder(new TermQueryBuilder(IndexFieldConstants.CATEGORYNAME_SEARCH,token)).boost(1.0f))
										.add(new ConstantScoreQueryBuilder(new TermQueryBuilder(IndexFieldConstants.BRANDNAME_SEARCH,token)).boost(1.0f))
										.add(new ConstantScoreQueryBuilder(new TermQueryBuilder(IndexFieldConstants.ATTRVALUE_SEARCH,token)))
										.add(new ConstantScoreQueryBuilder(new TermQueryBuilder(IndexFieldConstants.MERCHANTNAME_SEARCH,token)))
										;
//						keywordQueryBuilder.must(disMaxQueryBuilder);
						keywordQueryBuilder.should(disMaxQueryBuilder);
					}
					return keywordQueryBuilder;
				}
			}catch(Exception e){
				logger.error(e.getMessage(), e);
			}		
		 }
		return null;	
	}

	/**
	 * 构建搜索语句
	 * @param baseSearchRequest 搜索关键字,true ik_smart模式，false  ik_max_word模式
	 * @return
	 */
	public static QueryBuilder buildKeywordQueryBySmartType(BaseSearchRequest baseSearchRequest){
		if(StringUtils.isNotBlank(baseSearchRequest.getKeyword())){
			try{
				List<String> tokens = IKSegmentManager.segment(baseSearchRequest.getKeyword(),baseSearchRequest.getUseSmart());
				baseSearchRequest.setTokens(tokens);
				if(CollectionUtils.isNotEmpty(tokens)){
					BoolQueryBuilder keywordQueryBuilder=new BoolQueryBuilder();
					for(String token:tokens){
						DisMaxQueryBuilder disMaxQueryBuilder = new DisMaxQueryBuilder()
								.add(new ConstantScoreQueryBuilder(new TermQueryBuilder(IndexFieldConstants.TAG_WORDS,token))).boost(100.0f)
								.add(new ConstantScoreQueryBuilder(new TermQueryBuilder(IndexFieldConstants.CATEGORYNAME_SEARCH,token)).boost(1.0f))
								.add(new ConstantScoreQueryBuilder(new TermQueryBuilder(IndexFieldConstants.BRANDNAME_SEARCH,token)).boost(1.0f))
								.add(new ConstantScoreQueryBuilder(new TermQueryBuilder(IndexFieldConstants.ATTRVALUE_SEARCH,token)))
								.add(new ConstantScoreQueryBuilder(new TermQueryBuilder(IndexFieldConstants.MERCHANTNAME_SEARCH,token)))
								;
						if (baseSearchRequest.getIsMergeResult()){
							//求并集
							keywordQueryBuilder.should(disMaxQueryBuilder);
						}else {
							//求交集
							keywordQueryBuilder.must(disMaxQueryBuilder);
						}
					}
					return keywordQueryBuilder;
				}
			}catch(Exception e){
				logger.error(e.getMessage(), e);
			}
		}
		return null;
	}

    /**
     * 构建搜索语句
     * @param tokens 搜索关键字
     * @return
     */
    public static QueryBuilder buildKeywordQueryByWords(List<String> tokens){
        if(CollectionUtils.isNotEmpty(tokens)){
            try{
                if(CollectionUtils.isNotEmpty(tokens)){
                    BoolQueryBuilder keywordQueryBuilder=new BoolQueryBuilder();
                    for(String token:tokens){
                        DisMaxQueryBuilder disMaxQueryBuilder = new DisMaxQueryBuilder()
                                .add(new ConstantScoreQueryBuilder(new TermQueryBuilder(IndexFieldConstants.TAG_WORDS,token))).boost(100.0f)
                                .add(new ConstantScoreQueryBuilder(new TermQueryBuilder(IndexFieldConstants.CATEGORYNAME_SEARCH,token)).boost(1.0f))
                                .add(new ConstantScoreQueryBuilder(new TermQueryBuilder(IndexFieldConstants.BRANDNAME_SEARCH,token)).boost(1.0f))
                                .add(new ConstantScoreQueryBuilder(new TermQueryBuilder(IndexFieldConstants.ATTRVALUE_SEARCH,token)))
                                .add(new ConstantScoreQueryBuilder(new TermQueryBuilder(IndexFieldConstants.MERCHANTNAME_SEARCH,token)))
                                ;
//						keywordQueryBuilder.must(disMaxQueryBuilder);
                        keywordQueryBuilder.should(disMaxQueryBuilder);
                    }
                    return keywordQueryBuilder;
                }
            }catch(Exception e){
                logger.error(e.getMessage(), e);
            }
        }
        return null;
    }

	public static QueryBuilder fuzzyBuildKeywordQuery(String keyword){	
		if(StringUtils.isNotBlank(keyword)){	
			try{
				List<String> tokens = IKSegmentManager.segment(keyword);
				if(CollectionUtils.isNotEmpty(tokens)){
					BoolQueryBuilder keywordQueryBuilder=new BoolQueryBuilder();
					for(String token:tokens){
						BoolQueryBuilder tokenQueryBuilder=new BoolQueryBuilder();
						tokenQueryBuilder.should(new FuzzyQueryBuilder(IndexFieldConstants.TAG_WORDS,token).fuzziness(keywordFuzziness));
						tokenQueryBuilder.should(new FuzzyQueryBuilder(IndexFieldConstants.CATEGORYNAME_SEARCH,token).boost(100.0f).fuzziness(keywordFuzziness));
						tokenQueryBuilder.should(new FuzzyQueryBuilder(IndexFieldConstants.BRANDNAME_SEARCH,token).boost(200.0f).fuzziness(keywordFuzziness));
						tokenQueryBuilder.should(new FuzzyQueryBuilder(IndexFieldConstants.ATTRVALUE_SEARCH,token).fuzziness(keywordFuzziness));
						tokenQueryBuilder.should(new FuzzyQueryBuilder(IndexFieldConstants.MERCHANTNAME_SEARCH,token).fuzziness(keywordFuzziness));
						keywordQueryBuilder.should(tokenQueryBuilder);
					}
					return keywordQueryBuilder;
				}
			}catch(Exception e){
				logger.error(e.getMessage(), e);
			}
			
		 }
		return null;
		
	}
	
	public static QueryBuilder lessBuildKeywordQuery(String keyword){	
		if(StringUtils.isNotBlank(keyword)){	
			try{
				List<String> tokens = IKSegmentManager.segment(keyword);
				if(CollectionUtils.isNotEmpty(tokens)){
					BoolQueryBuilder keywordQueryBuilder=new BoolQueryBuilder();
					for(String token:tokens){
						BoolQueryBuilder tokenQueryBuilder=new BoolQueryBuilder();
						tokenQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.TAG_WORDS,token));
						tokenQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.CATEGORYNAME_SEARCH,token).boost(100.0f));
						tokenQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.BRANDNAME_SEARCH,token).boost(200.0f));
						tokenQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.ATTRVALUE_SEARCH,token));
						tokenQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.MERCHANTNAME_SEARCH,token));
						keywordQueryBuilder.should(tokenQueryBuilder);
					}
					keywordQueryBuilder.minimumNumberShouldMatch((int)Math.ceil(tokens.size()/2.0));
					return keywordQueryBuilder;
				}
			}catch(Exception e){
				logger.error(e.getMessage(), e);
			}
			
		 }
		return null;
		
	}
	public static QueryBuilder buildKeywordQueryForMerchantName(String keyword){	
		if(StringUtils.isNotBlank(keyword)){	
			try{
				List<String> tokens = IKSegmentManager.segment(keyword);
				if(CollectionUtils.isNotEmpty(tokens)){
					BoolQueryBuilder keywordQueryBuilder=new BoolQueryBuilder();
					for(String token:tokens){
						BoolQueryBuilder tokenQueryBuilder=new BoolQueryBuilder();
						tokenQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.MERCHANTNAME_SEARCH,token));
						keywordQueryBuilder.must(tokenQueryBuilder);
					}
					return keywordQueryBuilder;
				}
			}catch(Exception e){
				logger.error(e.getMessage(), e);
			}
			
		 }
		return null;
		
	}
	
	public static QueryBuilder buildKeywordQueryForMerchantProductName(String keyword){	
		if(StringUtils.isNotBlank(keyword)){	
			try{
				List<String> tokens = IKSegmentManager.segment(keyword);
				if(CollectionUtils.isNotEmpty(tokens)){
					BoolQueryBuilder keywordQueryBuilder=new BoolQueryBuilder();
					for(String token:tokens){
						BoolQueryBuilder tokenQueryBuilder=new BoolQueryBuilder();
						tokenQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.TAG_WORDS,token));
						keywordQueryBuilder.must(tokenQueryBuilder);
					}
					return keywordQueryBuilder;
				}
			}catch(Exception e){
				logger.error(e.getMessage(), e);
			}
			
		 }
		return null;
		
	}

	public static QueryBuilder brandNameBuildKeywordQuery(String keyword){
		if(StringUtils.isNotBlank(keyword)){
			try{
//				List<String> tokens = IKSegmentManager.segment(keyword); //由于这里考虑的是搜索的品牌名，所以不做拆分直接进行搜索build设置
				String token=keyword.toLowerCase().trim();
				BoolQueryBuilder keywordQueryBuilder=new BoolQueryBuilder();
				BoolQueryBuilder tokenQueryBuilder=new BoolQueryBuilder();
				tokenQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.BRANDNAME_SEARCH,token).boost(200.0f));
				keywordQueryBuilder.must(tokenQueryBuilder);
				return keywordQueryBuilder;
			}catch(Exception e){
				logger.error(e.getMessage(), e);
			}
		}
		return null;
	}
	
	public static QueryBuilder buildKeywordQuery(String keyword,String fieldName){	
		if(StringUtils.isNotBlank(keyword)){	
			try{
				List<String> tokens = IKSegmentManager.segment(keyword);
				if(CollectionUtils.isNotEmpty(tokens)){
					BoolQueryBuilder keywordQueryBuilder=new BoolQueryBuilder();
					for(String token:tokens){
						BoolQueryBuilder tokenQueryBuilder=new BoolQueryBuilder();
						tokenQueryBuilder.should(new TermQueryBuilder(fieldName,token));
						keywordQueryBuilder.must(tokenQueryBuilder);
					}
					return keywordQueryBuilder;
				}
			}catch(Exception e){
				logger.error(e.getMessage(), e);
			}		
		 }
		return null;	
	}
}
