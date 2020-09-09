package com.odianyun.search.whale.o2o.resp;

import org.elasticsearch.action.search.SearchResponse;

import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.o2o.O2OShopSearchResponse;

public interface O2OShopSearchResponseHandler {
	
	public void handle(SearchResponse searchResponse,O2OShopSearchResponse o2oShopSearchResponse) throws SearchException;

}
