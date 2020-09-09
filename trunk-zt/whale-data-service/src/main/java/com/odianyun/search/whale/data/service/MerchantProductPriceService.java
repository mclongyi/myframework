package com.odianyun.search.whale.data.service;

import java.util.List;
import java.util.Map;

import com.odianyun.search.whale.data.model.MerchantProductPrice;

public interface MerchantProductPriceService {
		
	
	/**
	 * 从DB读取商品价格数据
	 * @param merchantProductIds
	 * @return
	 */
	public Map<Long,MerchantProductPrice> getMerchantProductPricesByTable(List<Long> merchantProductIds, int companyId) throws Exception;

	public Map<Long,Double> getMerchantProductPricesByIds(List<Long> merchantProductIds, int companyId) throws Exception;

}
