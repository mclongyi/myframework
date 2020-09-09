package com.odianyun.search.whale.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.odianyun.architecture.caddy.SystemContext;
import com.odianyun.search.whale.api.model.SearchResponse;
import com.odianyun.search.whale.api.model.geo.Point;
import com.odianyun.search.whale.api.model.req.SearchRequest;
import com.odianyun.search.whale.api.model.req.ShopListSearchRequest;
import com.odianyun.search.whale.api.model.resp.ShopSearchResponse;
import com.odianyun.search.whale.api.model.resp.ShopSearchResult;
import com.odianyun.search.whale.api.service.SearchBusinessService;
import com.odianyun.search.whale.api.service.SearchClient;
import com.odianyun.search.whale.api.service.ShopService;
import com.odianyun.search.whale.resp.handler.ResponseHandler;
import org.apache.commons.collections.CollectionUtils;

public class RestSearchServiceTest extends AbstractTest{
	
	public static void main(String[] args) throws Exception{

		testShopListSearch();
	}

	public static void testCheckMerchantProductSaleArea() throws Exception{
		SearchBusinessService searchBusinessService = (SearchBusinessService)context.getBean("searchBusinessService");
		List<Long> ids = new ArrayList<Long>();
		SystemContext.setCompanyId(30L);
		ids.add(1049014806000047L);
		Map<Long,Boolean> retMap = searchBusinessService.checkMerchantProductSaleArea(ids,310115L);
		System.out.println(retMap);
	}

	public static void testShopListSearch() throws Exception{
		Point poi = new Point(121.582397,31.197224);
		ShopListSearchRequest shopListSearchRequest=new ShopListSearchRequest(poi,52);
		shopListSearchRequest.setAdditionalHotProduct(true);
		ShopService shopService = SearchClient.getShopService("test");
		ShopSearchResponse shopSearchResponse = shopService.search(shopListSearchRequest);
		if(shopSearchResponse!=null){
			System.out.println(shopSearchResponse.getTotalHit());
			List<ShopSearchResult> shopSearchResultList=shopSearchResponse.getShopResult();
			if(CollectionUtils.isNotEmpty(shopSearchResultList)){
				for(ShopSearchResult shopSearchResult:shopSearchResultList){
					System.out.println(shopSearchResult.getMerchant());
				}
			}
		}

	}

}
