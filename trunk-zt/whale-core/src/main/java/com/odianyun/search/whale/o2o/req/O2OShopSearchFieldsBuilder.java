package com.odianyun.search.whale.o2o.req;

import java.util.Arrays;
import com.odianyun.search.whale.api.model.o2o.O2OShopSearchRequest;
import com.odianyun.search.whale.common.RequestFieldsBuilder;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.index.api.common.IndexFieldConstants;

public class O2OShopSearchFieldsBuilder implements O2OShopSearchRequestBuilder{

	@Override
	public void build(ESSearchRequest esSearchRequest,
			O2OShopSearchRequest o2oShopSearchRequest) {
		RequestFieldsBuilder.innerBuild(esSearchRequest);
		String[] facet_fields = new String[] { IndexFieldConstants.MERCHANTCATEGORYID_SEARCH};
		esSearchRequest.setFacet_fields(Arrays.asList(facet_fields));
		esSearchRequest.setStart(0);
		esSearchRequest.setCount(Integer.MAX_VALUE);
		
	}

}
