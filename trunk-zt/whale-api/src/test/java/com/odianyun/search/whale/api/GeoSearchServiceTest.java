package com.odianyun.search.whale.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.geo.GeoSearchRequest;
import com.odianyun.search.whale.api.model.geo.GeoSearchResponse;
import com.odianyun.search.whale.api.model.geo.GeoSearchService;
import com.odianyun.search.whale.api.model.geo.Point;
import com.odianyun.search.whale.api.service.HessionServiceFactory;
import com.odianyun.search.whale.api.service.SearchClient;

public class GeoSearchServiceTest {
	
	public static void main(String[] args) throws Exception{
		System.setProperty("global.config.path","/Users/fishcus/JavaDev/data/env-199");
		String serviceUrl="http://192.168.20.199:8080/search/soa/SOAGeoSearchService";
		GeoSearchService geoSearchService=HessionServiceFactory.getService(serviceUrl, GeoSearchService.class);
		//GeoSearchRequest geoSearchRequest=new GeoSearchRequest(new Point(121.5988508516,31.2113062556),4);
		GeoSearchRequest geoSearchRequest=new GeoSearchRequest(new Point(121.605407,31.202175),11);
		GeoSearchResponse geoSearchResponse=geoSearchService.search(geoSearchRequest);
		System.out.println(geoSearchResponse);

//		geoSearchService = SearchClient.getGeoSearchService("test");
		checkIntersection(geoSearchService);
		
	}
	
	public static void checkIntersection(GeoSearchService searchService){
		List<Point> list = new ArrayList<Point>();
		Point point1 = new Point(121.605407,31.202175);
//		Point point2 = new Point(121.605945,31.198375);
		list.add(point1);
//		list.add(point2);
		try {
			Map<Point,Boolean> map = searchService.checkIntersection(3086l, list, 11);
			System.out.println(map);
		} catch (SearchException e) {
			//e.printStackTrace();
		}

	}

}
