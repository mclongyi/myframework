package com.odianyun.search.whale.o2o.req;

import org.elasticsearch.common.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;

import com.odianyun.search.whale.api.model.o2o.O2OShopSearchRequest;
import com.odianyun.search.whale.common.query.KeywordQueryBuilder;
import com.odianyun.search.whale.common.query.MerchantCategoryQueryBuilder;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.index.api.common.IndexFieldConstants;

public class O2OShopSearchQueryBuilder implements O2OShopSearchRequestBuilder{

	@Override
	public void build(ESSearchRequest esSearchRequest,
			O2OShopSearchRequest o2oShopSearchRequest) {
		BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();
		if(o2oShopSearchRequest.getCompanyId()!=null){
			TermQueryBuilder termQuery= new TermQueryBuilder(IndexFieldConstants.COMPANYID,o2oShopSearchRequest.getCompanyId());
			boolQueryBuilder.must(termQuery);
		}
		String keyword=o2oShopSearchRequest.getKeyword();
		if(StringUtils.isNotBlank(keyword)){
			QueryBuilder keywordueryBuilder=KeywordQueryBuilder.buildKeywordQuery(o2oShopSearchRequest.getKeyword());
			if(keywordueryBuilder!=null){
				boolQueryBuilder.must(keywordueryBuilder);
			}
		}
		Long merchantId=o2oShopSearchRequest.getMerchantId();
		if(merchantId!=null&&merchantId!=0){
			QueryBuilder queryBuilder=MerchantCategoryQueryBuilder.build(merchantId, o2oShopSearchRequest.getMerchantCategoryIds());
			if(queryBuilder!=null){
				boolQueryBuilder.must(queryBuilder);
			}
		}
		esSearchRequest.setQueryBuilder(boolQueryBuilder);
	}

}
