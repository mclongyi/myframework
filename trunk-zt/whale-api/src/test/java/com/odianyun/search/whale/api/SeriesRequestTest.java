package com.odianyun.search.whale.api;

import com.odianyun.search.whale.api.model.SearchResponse;
import com.odianyun.search.whale.api.model.req.SearchRequest;
import com.odianyun.search.whale.api.model.series.SeriesRequest;
import com.odianyun.search.whale.api.model.series.SeriesResponse;
import com.odianyun.search.whale.api.service.HessionServiceFactory;
import com.odianyun.search.whale.api.service.SearchService;

public class SeriesRequestTest {
	
	public static void main(String[] args) throws Exception{
		String serviceUrl="http://120.92.232.248:8080/search/soa/SOASearchService";
		SearchService searchService=HessionServiceFactory.getService(serviceUrl, SearchService.class);
		SeriesRequest seriesRequest=new SeriesRequest(156L,2);
		SeriesResponse searchResponse=searchService.searchBySeriesRequest(seriesRequest);
		System.out.println(searchResponse);
	}

}
