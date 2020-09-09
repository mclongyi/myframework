package com.odianyun.search.whale.api;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.odianyun.search.whale.api.model.MerchantProduct;
import com.odianyun.search.whale.api.model.o2o.O2OShopCategoryResponse;
import com.odianyun.search.whale.api.model.o2o.O2OShopSearchRequest;
import com.odianyun.search.whale.api.model.o2o.O2OShopSearchResponse;
import com.odianyun.search.whale.api.model.o2o.O2OShopSearchService;
import com.odianyun.search.whale.api.service.HessionServiceFactory;

public class O2OShopSearchServiceTest {
	
	public static void main(String[] args) throws Exception{
		String serviceUrl="http://120.92.232.5:8080/search/soa/SOAO2oShopSearchService";
		O2OShopSearchService o2OShopSearchService=HessionServiceFactory.getService(serviceUrl, O2OShopSearchService.class);
		O2OShopSearchRequest o2oShopSearchRequest=new O2OShopSearchRequest(1,2161L);
		O2OShopSearchResponse o2OShopSearchResponse=o2OShopSearchService.shopSearch(o2oShopSearchRequest);
//		List<O2OShopCategoryResponse> shopCategoryResponses =o2OShopSearchResponse.getShopCategoryResponse();
//		for(O2OShopCategoryResponse o2OShopCategoryResponse:shopCategoryResponses){
//			System.out.println(o2OShopCategoryResponse.getMerchantId()+" "+o2OShopCategoryResponse.getMerchantCategoryId()+" "
//					+o2OShopCategoryResponse.getMerchantCategoryName());
//			List<MerchantProduct> merchantProducts=o2OShopCategoryResponse.getMerchantProducts();
//			for(MerchantProduct mp:merchantProducts){
//				System.out.println(mp);
//			}
//			
//		}
		List<O2OShopCategoryResponse> shopCategoryResponses2 =o2OShopSearchResponse.getNavCategoryResponse();
		int i=0;
		int size=0;
		Set<Long> mpIds=new HashSet<Long>();
		for(O2OShopCategoryResponse o2OShopCategoryResponse:shopCategoryResponses2){
			System.out.println(++i+" "+o2OShopCategoryResponse.getMerchantCategoryId()+" "
					+o2OShopCategoryResponse.getMerchantCategoryName() +" mp size is "+o2OShopCategoryResponse.getMerchantProducts().size());
			size+=o2OShopCategoryResponse.getMerchantProducts().size();
			List<MerchantProduct> merchantProducts=o2OShopCategoryResponse.getMerchantProducts();
			for(MerchantProduct mp:merchantProducts){
				mpIds.add(mp.getId());
				System.out.println(mp);
			}
			
		}
		System.out.println("total size is "+size+" mpIds is "+mpIds.size());
	}

}
