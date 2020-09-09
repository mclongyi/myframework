package com.odianyun.search.whale.data.service;

import java.util.List;
import java.util.Map;

import com.odianyun.search.whale.data.model.Volume4Sale;

public interface Volume4SaleService {
	
	/**
	 * 根据商品Id集合 查询商品销售数量
	 * @param merchantProductIds 商品ID List集合
	 * @return
	 */
	public Map<Long , Long> getMerchantVolume4SaleByIds(List<Long> merchantProductIds, int companyId) throws Exception;
	
	/**
	 * 根据商品Id集合 查询商品销售数量和真实销量数据
	 * @param merchantProductIds 商品ID List集合
	 * @return
	 */
	public Map<Long , Volume4Sale> getMerchantVolume4SaleDetailByIds(List<Long> merchantProductIds, int companyId) throws Exception;

}
