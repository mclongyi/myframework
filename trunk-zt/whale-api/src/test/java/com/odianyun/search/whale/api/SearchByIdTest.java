package com.odianyun.search.whale.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.odianyun.search.whale.api.model.MerchantProduct;
import com.odianyun.search.whale.api.service.HessionServiceFactory;
import com.odianyun.search.whale.api.service.SearchService;

public class SearchByIdTest {
	
	public static void main(String[] args) throws Exception{    
		String serviceUrl="http://192.168.20.199:8080/search/soa/SOASearchService";
		SearchService searchService=HessionServiceFactory.getService(serviceUrl, SearchService.class);
		List<Long> ids=new ArrayList<Long>();
		ids.add(1021012200000300l);
//		ids.add(1373527l);
//		ids.add(1373534l);
//		ids.add(1373487l);
//		ids.add(1375054l);
//		ids.add(1372784l);
//		ids.add(1372745l);
		Map<Long,MerchantProduct> merchantProducts=searchService.searchById(ids, 11);
		System.out.println(merchantProducts);
	}

}
