package com.odianyun.search.whale.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.odianyun.architecture.caddy.SystemContext;
import com.odianyun.cc.client.spring.OccPropertiesLoaderUtils;
import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.ManagementType;
import com.odianyun.search.whale.api.model.MerchantProduct;
import com.odianyun.search.whale.api.model.SearchResponse;
import com.odianyun.search.whale.api.model.o2o.O2OShopSearchService;
import com.odianyun.search.whale.api.model.req.FilterType;
import com.odianyun.search.whale.api.model.req.SearchRequest;
import com.odianyun.search.whale.api.model.req.ShopSearchRequest;
import com.odianyun.search.whale.api.model.req.SortType;
import com.odianyun.search.whale.api.service.HessionServiceFactory;
import com.odianyun.search.whale.api.service.SearchBusinessService;
import com.odianyun.search.whale.api.service.SearchClient;
import com.odianyun.search.whale.api.service.SearchService;

public class SearchBusinessServiceTest extends AbstractTest{
	
	public static void main(String[] args) throws Exception{    
		String serviceUrl="http://192.168.8.17:8080/search/soa/SOASearchBusinessService";
		//String serviceUrl1 = "http://172.16.2.31:8080/whale-core/soa/SOASearchService";
		//SearchBusinessService searchBusinessService=HessionServiceFactory.getService(serviceUrl, SearchBusinessService.class);
		SearchBusinessService searchBusinessService = SearchClient.getSearchBusinessService("ad-whale");
		List<Long> ids = new ArrayList<Long>();
		ids.add(1049014806000047L);
		SystemContext.setCompanyId(30L);
		Map<Long,Boolean> retMap = searchBusinessService.checkMerchantProductSaleArea(ids,310115L);
		System.out.println(retMap);






	}

}
