package com.odianyun.search.whale.tracker;

import com.odianyun.search.whale.api.model.SearchResponse;
import com.odianyun.search.whale.api.model.req.BaseSearchRequest;

public class TrackContext {

	public BaseSearchRequest request;
	public SearchResponse response;
	
	public TrackContext(BaseSearchRequest request, SearchResponse response) {
		this.request =  request;
		this.response = response;
	}
}
