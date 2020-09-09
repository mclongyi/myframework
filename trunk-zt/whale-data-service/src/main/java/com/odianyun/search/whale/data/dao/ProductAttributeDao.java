package com.odianyun.search.whale.data.dao;

import java.util.List;

import com.odianyun.search.whale.data.model.MerchantProdAttValue;
import com.odianyun.search.whale.data.model.MerchantProductAttributeValue;
import com.odianyun.search.whale.data.model.ProductAttributeValue;

public interface ProductAttributeDao {
	
//	List<ProductAttributeValue> queryAllProductAttributeValues(int companyId) throws Exception;
	
	List<ProductAttributeValue> queryProductAttributeValues(List<Long> productIds, int companyId) throws Exception;

	List<MerchantProductAttributeValue> queryMerchantProductAttributeValues(List<Long> merchantProductIds,int companyId) throws Exception;

	List<MerchantProdAttValue> queryMerchantProdAttValues(List<Long> merchantProductIds, int companyId) throws Exception;
}
