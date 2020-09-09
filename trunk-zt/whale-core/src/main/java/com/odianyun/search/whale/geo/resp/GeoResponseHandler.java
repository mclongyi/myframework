package com.odianyun.search.whale.geo.resp;

import com.odianyun.search.whale.api.model.geo.GeoSearchResponse;

public interface GeoResponseHandler {
	
	public void handle(org.elasticsearch.action.search.SearchResponse esSearchResponse,GeoSearchResponse geoSearchResponse) throws Exception;

}
