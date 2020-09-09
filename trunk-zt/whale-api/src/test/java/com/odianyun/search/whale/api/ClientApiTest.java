package com.odianyun.search.whale.api;

import com.odianyun.search.whale.api.model.req.SearchRequest;
import com.odianyun.search.whale.api.model.req.ShopSearchRequest;
import com.odianyun.search.whale.api.service.SearchClient;
import com.odianyun.search.whale.api.service.SearchService;

import junit.framework.TestCase;

public class ClientApiTest extends TestCase {

public void testClientApi() {
		
		System.setProperty("global.config.path", "/data/env");
		SearchService client = SearchClient.getSearchService("test");
		
		SearchRequest req = new SearchRequest(1);
		req.setKeyword("测试120");
		for(int i = 0; i < 1; ++i)
			client.search(req);
		
//		ShopSearchRequest reqshop = new ShopSearchRequest(99L);
//		reqshop.setKeyword("shop search api");
//		for(int i = 0; i < 1; ++i)
//			client.shopSearch(reqshop);
		
	}
}
