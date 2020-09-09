package com.odianyun.search.whale.data.service;

import java.util.List;
import java.util.Map;

import com.odianyun.search.whale.data.model.MerchantProductAttributeValue;
import com.odianyun.search.whale.data.model.ProductAttributeValue;

public interface ProductAttributeService {
	
//	public void reload() throws Exception;
	
//	List<ProductAttributeValue> queryProductAttributeValues(Long productId) throws Exception;
	
	Map<Long,List<ProductAttributeValue>> queryProductAttributeValuesTable(List<Long> productIds, int companyId) throws Exception;
	//查询商品的属性
	Map<Long,List<MerchantProductAttributeValue>> queryMerchantProductAttributeValuesByTable(List<Long> merchantProductIds, int companyId)throws Exception;

	Map<Long,Map<Long, String>> queryMerchantProductAttributeValueCustom(List<Long> merchantProductIds, int companyId) throws Exception;
}
