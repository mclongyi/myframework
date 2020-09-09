package com.odianyun.search.whale.data.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.odianyun.search.whale.data.model.Product;

public interface ProductService {
	
//	public void reload() throws Exception;
	
//	public Map<Long,Product> getAllProducts() throws Exception;
	
	public Map<Long,Product> getProducts(List<Long> productIds, int companyId) throws Exception;

	public Map<Long, List<Long>> getMerchantCateTreeNodeIds(List<Long> merchantProductIds, int companyId) throws Exception;

	//所有的商品以及对应的关联ean码
	public Map<Long, List<String>> getAllMp_Eans(List<Long> merchantProductIds, int companyId) throws Exception;

	public Map<String,List<String>> getSrcbarcodes(List<String> destBarcodes,Integer companyId) throws Exception;



}
