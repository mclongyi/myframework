package com.odianyun.search.whale.resp.handler;

import com.odianyun.search.whale.api.model.req.BaseSearchRequest;
import com.odianyun.search.whale.es.request.ESSearchRequest;


public interface ResponseHandler<T extends BaseSearchRequest> {

	public void handle(org.elasticsearch.action.search.SearchResponse esSearchResponse,
					   com.odianyun.search.whale.api.model.SearchResponse searchResponse,
					   ESSearchRequest esSearchRequest,T searchRequest) throws Exception;

}
