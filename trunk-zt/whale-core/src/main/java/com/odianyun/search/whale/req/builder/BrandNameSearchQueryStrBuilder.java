package com.odianyun.search.whale.req.builder;

import com.odianyun.search.whale.api.model.req.BaseSearchRequest;
import com.odianyun.search.whale.common.query.KeywordQueryBuilder;
import com.odianyun.search.whale.index.api.common.IndexFieldConstants;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;

public class BrandNameSearchQueryStrBuilder extends SearchQueryStrBuilder {

	@Override
	protected QueryBuilder buildKeywordQuery(BoolQueryBuilder boolQueryBuilder,
			BaseSearchRequest baseSearchRequest) {
		return KeywordQueryBuilder.buildKeywordQuery(baseSearchRequest.getKeyword(),IndexFieldConstants.BRANDNAME_SEARCH);
	}


}
