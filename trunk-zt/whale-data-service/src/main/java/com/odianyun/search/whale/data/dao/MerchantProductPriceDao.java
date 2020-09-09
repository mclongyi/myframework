package com.odianyun.search.whale.data.dao;

import java.util.List;

import com.odianyun.search.whale.data.model.MerchantProductPrice;

public interface MerchantProductPriceDao {
	
	List<MerchantProductPrice> queryMerchantProductPrice(List<Long> merchantProductIds, int companyId) throws Exception;

	List<MerchantProductPrice> queryMerchantProductPromotionPrice(List<Long> merchantProductIds, int companyId) throws Exception;

}
