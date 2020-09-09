package com.odianyun.search.whale.geo.req;

import com.odianyun.search.whale.api.model.geo.GeoSearchRequest;
import com.odianyun.search.whale.es.request.ESSearchRequest;

public interface GeoRequestBuilder {
	
	public void build(ESSearchRequest esSearchRequest,GeoSearchRequest geoSearchRequest);

}
