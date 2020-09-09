package com.odianyun.search.whale.req.builder;

import com.odianyun.search.whale.api.model.req.BaseSearchRequest;
import com.odianyun.search.whale.es.request.ESSearchRequest;

public interface RequestBuilder {
	
	public void build(ESSearchRequest esSearchRequest,BaseSearchRequest searchRequest);


}
