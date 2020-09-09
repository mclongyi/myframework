package com.odianyun.search.whale.common;

import com.odianyun.search.whale.api.model.ManagementType;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.index.api.common.IndexFieldConstants;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class QueryByIdBuilder {
	
	public static void build(ESSearchRequest esSearchRequest,List<Long> mpIdList,Integer companyId,int start,int count){

		BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
		TermQueryBuilder companyIdQuery = new TermQueryBuilder(IndexFieldConstants.COMPANYID, companyId);
		boolQueryBuilder.must(companyIdQuery);
		BoolQueryBuilder mpIdsBoolQueryBuilder = new BoolQueryBuilder();
		boolQueryBuilder.must(mpIdsBoolQueryBuilder);
		for (Long mpId : mpIdList) {
			TermQueryBuilder mpIdQuery = new TermQueryBuilder(IndexFieldConstants.ID, mpId);
			mpIdsBoolQueryBuilder.should(mpIdQuery);
		}
		boolQueryBuilder.must(new TermQueryBuilder(IndexFieldConstants.MANAGEMENT_STATE, ManagementType.ON_SHELF.getCode()));

		esSearchRequest.setQueryBuilder(boolQueryBuilder);
		esSearchRequest.setStart(start);
		esSearchRequest.setCount(count);
		RequestFieldsBuilder.build(esSearchRequest);
	}


}
