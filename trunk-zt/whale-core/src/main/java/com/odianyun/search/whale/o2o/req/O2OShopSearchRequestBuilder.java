package com.odianyun.search.whale.o2o.req;

import com.odianyun.search.whale.api.model.o2o.O2OShopSearchRequest;
import com.odianyun.search.whale.es.request.ESSearchRequest;

public interface O2OShopSearchRequestBuilder {
	
	public void build(ESSearchRequest esSearchRequest,O2OShopSearchRequest o2OShopSearchRequest);

}
