package com.odianyun.search.whale.api;

import java.util.ArrayList;
import java.util.List;

import com.odianyun.search.whale.api.model.MerchantProduct;
import com.odianyun.search.whale.api.model.req.SortType;
import com.odianyun.search.whale.api.model.selectionproduct.PromotionProductSearchRequest;
import com.odianyun.search.whale.api.model.selectionproduct.PromotionProductSearchResponse;
import com.odianyun.search.whale.api.model.selectionproduct.SelectionMerchantProductSearchResponse;
import com.odianyun.search.whale.api.model.selectionproduct.SelectionProductSearchRequest;
import com.odianyun.search.whale.api.model.selectionproduct.SelectionProductSearchResponse;
import com.odianyun.search.whale.api.model.selectionproduct.SelectionProductSearchService;
import com.odianyun.search.whale.api.service.HessionServiceFactory;

public class SelectionProductSearchTest {
	
	public static void main(String[] args) throws Exception{
		String serviceUrl="http://192.168.6.27:8080/search/soa/SOASelectionProductSearchService";
		SelectionProductSearchService selectionProductSearchService=HessionServiceFactory.getService(serviceUrl, SelectionProductSearchService.class);
		SelectionProductSearchRequest selectionProductSearchRequest=new SelectionProductSearchRequest(10);
		List<SortType> sortTypeList = new ArrayList<>();

		sortTypeList.add(SortType.volume4sale_desc);
		//List<Long> merchantIds=new ArrayList<Long>();
//		merchantIds.add(101L);
//		selectionProductSearchRequest.setMerchantIds(merchantIds);
		//selectionProductSearchRequest.setCompanyId(2);
		List<String> codes=new ArrayList<String>();
		codes.add("45671535X48177S7");
//		selectionProductSearchRequest.setCodes(codes);
//		selectionProductSearchRequest.setKeyword("运动");
		selectionProductSearchRequest.setSortTypeList(sortTypeList);
		SelectionProductSearchResponse selectionProductSearchResponse=selectionProductSearchService.selectionSearch(selectionProductSearchRequest);
		System.out.println(selectionProductSearchResponse.getSelectionProducts().size());
		
		selectionProductSearchRequest.setStart(0);
//		selectionProductSearchRequest.setCount(1000);
		SelectionMerchantProductSearchResponse selectionMerchantProductSearchResponse = selectionProductSearchService.selectionSearch2(selectionProductSearchRequest);
		
		System.out.println(selectionMerchantProductSearchResponse.getMerchantProducts().size());
		for(MerchantProduct p:selectionMerchantProductSearchResponse.getMerchantProducts()){
//			System.out.println(p.getVolume4sale());
		}
		List<Long> merchantIds = new ArrayList<>();
		merchantIds.add(101l);
		PromotionProductSearchRequest promotionProductSearchRequest = new PromotionProductSearchRequest(10,merchantIds);
//		promotionProductSearchRequest.setBrandName("约瑟夫");
//		promotionProductSearchRequest.setCategoryName("妈咪上衣");
		List<Long> excludeBrandIdList = new ArrayList<>();
		excludeBrandIdList.add(19l);
//		promotionProductSearchRequest.setExcludeBrandIdList(excludeBrandIdList);
		List<Long> excludeCategoryIdList = new ArrayList<>();
		excludeCategoryIdList.add(129l);
//		promotionProductSearchRequest.setExcludeCategoryIdList(excludeCategoryIdList);
		promotionProductSearchRequest.setStart(0);
//		promotionProductSearchRequest.setCount(1000);
		List<String> productCodes = new ArrayList<>();
		productCodes.add("AL9GT0V07H");
//		promotionProductSearchRequest.setProductCodes(productCodes);
		promotionProductSearchRequest.setSortTypeList(sortTypeList);
		PromotionProductSearchResponse promotionProductSearchResponse= selectionProductSearchService.promotionSelectionSearch(promotionProductSearchRequest);
		

		System.out.println(promotionProductSearchResponse.getMerchantProducts().size());
		for(MerchantProduct p:promotionProductSearchResponse.getMerchantProducts()){
//			System.out.println(p.getVolume4sale());
		}
	}

}
