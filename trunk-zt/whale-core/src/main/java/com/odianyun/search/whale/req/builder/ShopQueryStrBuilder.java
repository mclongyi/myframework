package com.odianyun.search.whale.req.builder;


import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import com.odianyun.search.whale.api.model.req.BaseSearchRequest;
import com.odianyun.search.whale.api.model.req.ShopSearchRequest;
import com.odianyun.search.whale.common.query.MerchantCategoryQueryBuilder;

public class ShopQueryStrBuilder  extends BaseQueryStrBuilder {

	@Override
	protected void buildOtherQuery(BoolQueryBuilder boolQueryBuilder,BoolQueryBuilder aggregationBoolQueryBuilder,
			BaseSearchRequest baseSearchRequest) {
		ShopSearchRequest shopSearchReq =(ShopSearchRequest)baseSearchRequest;
		QueryBuilder queryBuilder=MerchantCategoryQueryBuilder.build(shopSearchReq.getMerchantIdList(), shopSearchReq.getMerchantCategoryIds());
		if(queryBuilder!=null){
			boolQueryBuilder.must(queryBuilder);
			aggregationBoolQueryBuilder.must(queryBuilder);
		}
	}


}
