package com.odianyun.search.whale.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.ManagementType;
import com.odianyun.search.whale.api.model.MerchantProduct;
import com.odianyun.search.whale.api.model.SearchResponse;
import com.odianyun.search.whale.api.model.geo.Point;
import com.odianyun.search.whale.api.model.o2o.O2OShopSearchService;
import com.odianyun.search.whale.api.model.req.*;
import com.odianyun.search.whale.api.model.resp.HotSearchResponse;
import com.odianyun.search.whale.api.service.HessionServiceFactory;
import com.odianyun.search.whale.api.service.SearchClient;
import com.odianyun.search.whale.api.service.SearchService;

public class SearchServiceTest extends AbstractTest{
	
	public static void main(String[] args) throws Exception{    
		String serviceUrl="http://192.168.20.199:8080/search/soa/SOASearchService";
		//String serviceUrl1 = "http://172.16.2.31:8080/whale-core/soa/SOASearchService";
		SearchService searchService=HessionServiceFactory.getService(serviceUrl, SearchService.class);
		
		SearchRequest searchRequest2=new SearchRequest(30);
		/*List<Long> navCategoryIds=new ArrayList<Long>();
		navCategoryIds.add(1029L);
		searchRequest2.setNavCategoryIds(navCategoryIds);
		Map<Long, List<Long>> attrItemValuesMap=new HashMap<Long, List<Long>>();
		List<Long> valueIds=new ArrayList<Long>();
		valueIds.add(249L);
		attrItemValuesMap.put(157L, valueIds);
		searchRequest2.setAttrItemValuesMap(attrItemValuesMap);
		//searchRequest2.setAttrValueIds(valueIds);
		searchRequest2.setCompanyId(2);*/
		searchRequest2.setKeyword("百事可乐");
		searchRequest2.setCount(10000);
//		searchRequest2.setCount(10000);
		List<Long> areaCode = new ArrayList<Long>();
		//areaCode.add(130900L);
		//searchRequest2.setSaleAreaCode(areaCode);
		searchRequest2.setKeyword("男装外套");
//		searchRequest2.setCount(10000);
//		searchRequest2.setManagementState(ManagementType.VERIFIED);
		SearchResponse searchResponse2=searchService.search(searchRequest2);
		System.out.println(searchResponse2.getTotalHit());
		System.out.println(searchResponse2.getMerchantProductIds());


		ShopSearchRequest ShopSearchRequest = new ShopSearchRequest(11,1038012102000039l);
		ShopSearchRequest.setStart(0);
		ShopSearchRequest.setCount(10);
//		ShopSearchRequest.setKeyword("蜂蜜");
		SearchResponse shopResponse = searchService.shopSearch(ShopSearchRequest);
		System.out.println(shopResponse.totalHit);
		for(MerchantProduct mp : shopResponse.getMerchantProductResult()){
			System.out.println(mp);
		}
		System.out.println("--------------");

		HotSearchRequest hotSearchRequest = new HotSearchRequest(11);
		hotSearchRequest.setStart(5);
		hotSearchRequest.setCount(5);
		Point point = new Point(121.600543,31.199196);
		hotSearchRequest.setPoint(point);
		HotSearchResponse hotSearchResponse = searchService.hotSearch(hotSearchRequest);
		System.out.println(hotSearchResponse.getTotalHit());

		System.out.println(hotSearchResponse.getMerchantProductIds());
		System.out.println(hotSearchResponse.getMerchantProductResult());

	}

	public void searchMpByEans(){

	}

}
