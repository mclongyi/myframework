package com.odianyun.search.whale.index.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;

import com.odianyun.search.whale.analysis.ik.IKSegment;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.index.api.common.IndexFieldConstants;
import com.odianyun.search.whale.index.full.MerchantProductIndexSwitcher;

public class ValidateQueryBuilder {
	
	static Logger logger = Logger.getLogger(ValidateQueryBuilder.class);

	static IKSegment ikSeg =new IKSegment(true);

	public static void build(ESSearchRequest esSearchRequest,String line, String searchType){
		BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();
		List<Long> categoryIds=null;
		if(MerchantProductIndexSwitcher.KEYWORD.equals(searchType)){
			buildKeywordQuery(boolQueryBuilder,line);
		}else if(MerchantProductIndexSwitcher.CATEGORY.equals(searchType)){
			categoryIds = new ArrayList<Long>();
			String ids[] = line.split(",");
			for(String id : ids){
				categoryIds.add(Long.valueOf(id.trim()));
			}
		}
		if(CollectionUtils.isNotEmpty(categoryIds)){
			BoolQueryBuilder categoryIdQueryBuilder=new BoolQueryBuilder();
			for(Long categoryId:categoryIds){
				categoryIdQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.CATEGORYID_SEARCH,String.valueOf(categoryId)));
			}
			boolQueryBuilder.must(categoryIdQueryBuilder);
		}
	
		/*List<Long> attrValueIds=searchRequest.getAttrValueIds();
		if(CollectionUtils.isNotEmpty(attrValueIds)){
			BoolQueryBuilder attrValueIdQueryBuilder=new BoolQueryBuilder();
			for(Long attrValueId:attrValueIds){
				attrValueIdQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.ATTRVALUEID_SEARCH,String.valueOf(attrValueId)));
			}
			boolQueryBuilder.must(attrValueIdQueryBuilder);
		}

		List<Long> brandIds=searchRequest.getBrandIds();
		if(CollectionUtils.isNotEmpty(brandIds)){
			BoolQueryBuilder brandIdQueryBuilder=new BoolQueryBuilder();
			for(Long brandId:brandIds){
				brandIdQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.BRANDID_SEARCH,String.valueOf(brandId)));
			}
			boolQueryBuilder.must(brandIdQueryBuilder);
		}
		List<Integer> coverProvinceIds=searchRequest.getCoverProvinceIds();
		if(CollectionUtils.isNotEmpty(coverProvinceIds)){
			BoolQueryBuilder coverProvinceIdQueryBuilder=new BoolQueryBuilder();
			for(Integer coverProvinceId:coverProvinceIds){
				coverProvinceIdQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.COVER_PROVINCE_ID,String.valueOf(coverProvinceId)));
			}
			boolQueryBuilder.must(coverProvinceIdQueryBuilder);
		}*/
		
		esSearchRequest.setQueryBuilder(boolQueryBuilder);
	}

	protected static void buildKeywordQuery(BoolQueryBuilder boolQueryBuilder,String keyword){
		if(keyword!=null && !"".equals(keyword)){
			/*MultiMatchQueryBuilder keywordQueryBuilder = QueryBuilders.multiMatchQuery(keyword);
			// 设置搜索的字段和权重
			keywordQueryBuilder.field(IndexFieldConstants.TAG_WORDS,1.0f);
			keywordQueryBuilder.field(IndexFieldConstants.CATEGORYNAME_SEARCH,100.0f);
			keywordQueryBuilder.field(IndexFieldConstants.BRANDNAME_SEARCH,200.0f);
			keywordQueryBuilder.field(IndexFieldConstants.ATTRVALUE_SEARCH,1.0f);
			keywordQueryBuilder.field(IndexFieldConstants.MERCHANTNAME_SEARCH,1.0f);
			// 设置搜索关键词的关系为AND
			keywordQueryBuilder.operator(MatchQueryBuilder.Operator.AND);
			keywordQueryBuilder.type(MatchQueryBuilder.Type.BOOLEAN);
			boolQueryBuilder.must(keywordQueryBuilder);*/
			try{
				List<String> tokens = ikSeg.segment(keyword);
				if(CollectionUtils.isNotEmpty(tokens)){
					BoolQueryBuilder keywordQueryBuilder=new BoolQueryBuilder();
					for(String token:tokens){
						BoolQueryBuilder tokenQueryBuilder=new BoolQueryBuilder();
						tokenQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.TAG_WORDS,token));
						tokenQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.CATEGORYNAME_SEARCH,token).boost(100.0f));
						tokenQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.BRANDNAME_SEARCH,token).boost(200.0f));
						tokenQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.ATTRVALUE_SEARCH,token));
						tokenQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.MERCHANTNAME_SEARCH,token));
						keywordQueryBuilder.must(tokenQueryBuilder);
					}
					boolQueryBuilder.must(keywordQueryBuilder);
				}
			}catch(Exception e){
				logger.error(e.getMessage(), e);
			}
		 }
	}
}
