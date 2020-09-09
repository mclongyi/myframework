package com.odianyun.search.whale.req.builder;

import java.util.Arrays;
import com.odianyun.search.whale.api.model.req.BaseSearchRequest;
import com.odianyun.search.whale.common.RequestFieldsBuilder;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.index.api.common.IndexFieldConstants;

public class FieldsBuilder implements RequestBuilder {

	@Override
	public void build(ESSearchRequest esSearchRequest, BaseSearchRequest searchRequest) {

		RequestFieldsBuilder.build(esSearchRequest);
		if(searchRequest.isAggregation()){
			String[] facet_fields = new String[] { IndexFieldConstants.CATEGORYID,
					IndexFieldConstants.ATTRVALUEID_SEARCH, IndexFieldConstants.BRANDID_SEARCH, IndexFieldConstants.NAVCATEGORYID_SEARCH};
			esSearchRequest.setFacet_fields(Arrays.asList(facet_fields));
		}
		esSearchRequest.setStart(searchRequest.getStart());
		esSearchRequest.setCount(searchRequest.getCount());
	}

}
