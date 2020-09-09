package com.odianyun.search.whale.common.query;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import com.odianyun.search.whale.index.api.common.IndexFieldConstants;

public class MerchantCategoryQueryBuilder {
	
	public static QueryBuilder build(Long merchantId,List<Long> merchantCategoryIds){
		BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();
		if(merchantId!=null){
			boolQueryBuilder.must(new TermQueryBuilder(IndexFieldConstants.MERCHANTID,String.valueOf(merchantId)));
		}
		if(CollectionUtils.isNotEmpty(merchantCategoryIds)){
			BoolQueryBuilder merCategoryIdQueryBuilder = new BoolQueryBuilder();
			for(Long merCategoryId:merchantCategoryIds){
				merCategoryIdQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.MERCHANTCATEGORYID_SEARCH,String.valueOf(merCategoryId)));
			}
			boolQueryBuilder.must(merCategoryIdQueryBuilder);
		}
		return boolQueryBuilder;
	}

	public static QueryBuilder build(List<Long> merchantIdList,List<Long> merchantCategoryIds){
		BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();
		if(CollectionUtils.isNotEmpty(merchantIdList)){
			BoolQueryBuilder merchantIdQueryBuilder = new BoolQueryBuilder();
			for(Long merchantId : merchantIdList){
				merchantIdQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.MERCHANTID,String.valueOf(merchantId)));
			}
			boolQueryBuilder.must(merchantIdQueryBuilder);
		}
		if(CollectionUtils.isNotEmpty(merchantCategoryIds)){
			BoolQueryBuilder merCategoryIdQueryBuilder = new BoolQueryBuilder();
			for(Long merCategoryId:merchantCategoryIds){
				merCategoryIdQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.MERCHANTCATEGORYID_SEARCH,String.valueOf(merCategoryId)));
			}
			boolQueryBuilder.must(merCategoryIdQueryBuilder);
		}
		return boolQueryBuilder;
	}

}
