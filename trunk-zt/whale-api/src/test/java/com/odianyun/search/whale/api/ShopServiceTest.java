package com.odianyun.search.whale.api;

import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.SearchResponse;
import com.odianyun.search.whale.api.model.geo.Point;
import com.odianyun.search.whale.api.model.req.SearchRequest;
import com.odianyun.search.whale.api.model.req.ShopListSearchRequest;
import com.odianyun.search.whale.api.model.resp.ShopSearchResponse;
import com.odianyun.search.whale.api.model.resp.ShopSearchResult;
import com.odianyun.search.whale.api.service.HessionServiceFactory;
import com.odianyun.search.whale.api.service.SearchClient;
import com.odianyun.search.whale.api.service.SearchService;
import com.odianyun.search.whale.api.service.ShopService;
import org.apache.commons.lang.StringUtils;

public class ShopServiceTest extends AbstractTest{
	
	public static void main(String[] args) throws Exception{
		System.setProperty("global.config.path","/Users/fishcus/JavaDev/data/env-199");

		String serviceUrl="http://192.168.20.199:8080/search/soa/SOAShopService";
		//String serviceUrl1 = "http://172.16.2.31:8080/whale-core/soa/SOASearchService";
		ShopService shopService=HessionServiceFactory.getService(serviceUrl, ShopService.class);

//		shopService = SearchClient.getShopService("test");
		Point point = new Point(121.600543,31.199196);
		ShopListSearchRequest shopRequest = new ShopListSearchRequest(point,11);
		shopRequest.setAdditionalHotProduct(true);
//		shopRequest.setCount(100);
//		shopRequest.setKeyword("李松");
		shopRequest.setUserId("11111");
//		shopRequest.setMerchantCode("supermarket");
		ShopSearchResponse response=shopService.search(shopRequest);
		System.out.println(response.getTotalHit());
		for(ShopSearchResult result : response.getShopResult()){
			System.out.println(result.getMerchant());
			System.out.println(result.getMerchantProductList());
			System.out.println();
		}
//		System.out.println(response.getShopResult().get(2).getMerchantProductList().size());

//		validateRequest(shopRequest);


	}


	private static void validateRequest(ShopListSearchRequest shopRequest){
		if(shopRequest.getCompanyId()==null){
			throw new SearchException("companyId is null");
		}
		if(StringUtils.isBlank(shopRequest.getAddress()) && null == shopRequest.getPoint()){
			throw new SearchException("Address and Point is null ");
		}
		Point point = shopRequest.getPoint();
		if(point != null){
			Double latitude = point.getLatitude();
			if(latitude == null || latitude < -1 *90 || latitude > 90){
				throw new SearchException("latitude is illegal ");
			}
			Double longitude = point.getLongitude();
			if(longitude == null || longitude < -1*180 || longitude > 180){
				throw new SearchException("longitude is illegal ");
			}
		}

	}

}
