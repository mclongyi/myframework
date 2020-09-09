package com.odianyun.search.whale.api;

import java.net.URLEncoder;

import org.apache.http.client.utils.URLEncodedUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.geo.GeoDistanceSearchRequest;
import com.odianyun.search.whale.api.model.geo.GeoDistanceSearchRequest.Distance;
import com.odianyun.search.whale.api.model.geo.GeoSearchResponse;
import com.odianyun.search.whale.api.model.geo.GeoSearchService;
import com.odianyun.search.whale.api.model.geo.Point;
import com.odianyun.search.whale.api.service.HessionServiceFactory;
import com.odianyun.search.whale.common.util.HttpClientUtil;

public class GeoDistanceSearchServiceTest {
	
	public static void main(String[] args) throws Exception{
		String serviceUrl="http://192.168.8.17:8080/search/soa/SOAGeoSearchService";
		GeoSearchService geoSearchService=HessionServiceFactory.getService(serviceUrl, GeoSearchService.class);
//		GeoDistanceSearchRequest geoDistanceSearchRequest=new GeoDistanceSearchRequest(new Point(121.600543, 31.199196), 4, new GeoDistanceSearchRequest.Distance("6km"));
//		GeoSearchResponse geoSearchResponse=geoSearchService.search(geoDistanceSearchRequest);
//		System.out.println(geoSearchResponse);
		
		checkIntersection(geoSearchService);
		
	}
	
	public static void checkIntersection(GeoSearchService geoSearchService) throws Exception{	
		try {
			Distance distance=new Distance(5d);
			GeoDistanceSearchRequest distanceSearchRequest=new GeoDistanceSearchRequest("上海市浦东新区浦东软件园y1座",30,distance);
			GeoSearchResponse response=geoSearchService.search(distanceSearchRequest);	
			System.out.println(response);
			System.out.println(response.getMerchants().size());
			distanceSearchRequest=new GeoDistanceSearchRequest(new Point(121.602919,31.199037),30,distance);
			response=geoSearchService.search(distanceSearchRequest);	
			System.out.println(response);
			System.out.println(response.getMerchants().size());
			String map_url="http://restapi.amap.com/v3/geocode/geo?key=fe8a826a0ac9700f9b5910ea5bc37965";
			String address=URLEncoder.encode("广州市1406室海天网联营销传播机构");
			JSONObject jsonObject=HttpClientUtil.doGet2JSONObject(map_url+"&address="+address);
			JSONArray jsonArray=jsonObject.getJSONArray("geocodes");
			String location=(String) jsonArray.getJSONObject(0).get("location");
			System.out.println(location);
			
			address="上海市浦东新区亮秀路112号";
			jsonObject=HttpClientUtil.doGet2JSONObject(map_url+"&address="+address);
			jsonArray=jsonObject.getJSONArray("geocodes");
			location=(String) jsonArray.getJSONObject(0).get("location");
			System.out.println(location);
		} catch (SearchException e) {
			//e.printStackTrace();
		}
	}

}
