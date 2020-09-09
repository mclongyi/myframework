package com.odianyun.search.whale.req.builder;

import com.odianyun.search.whale.api.model.req.BaseSearchRequest;
import com.odianyun.search.whale.common.query.KeywordQueryBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;

public class LessSearchQueryStrBuilder extends SearchQueryStrBuilder {


	@Override
	protected QueryBuilder buildKeywordQuery(BoolQueryBuilder boolQueryBuilder,
			BaseSearchRequest baseSearchRequest) {
		//return KeywordQueryBuilder.lessBuildKeywordQuery(baseSearchRequest.getKeyword());
		/**
		 * 不需要调用lessBuildKeywordQuery,因为传入的keyword已经处理了
		 */
		if (baseSearchRequest.getTokens().isEmpty()){
			//原来的逻辑
			return KeywordQueryBuilder.buildKeywordQuery(baseSearchRequest.getKeyword());
		}else {
			//新加的逻辑：0结果的时候，直接进行查询
			return KeywordQueryBuilder.buildKeywordQueryByWords(baseSearchRequest.getTokens());
		}
	}

	
	

}
