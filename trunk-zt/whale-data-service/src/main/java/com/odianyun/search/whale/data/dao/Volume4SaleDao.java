package com.odianyun.search.whale.data.dao;

import java.util.List;

import com.odianyun.search.whale.data.model.MerchantProductSaleOffset;
import com.odianyun.search.whale.data.model.MerchantProductVolume4Sale;

public interface Volume4SaleDao {
	
	/**
	 * 查询所有商品的销售数量--按照页数查询排序
	 */
	List<MerchantProductVolume4Sale> queryVolume4Sale(List<Long> merchantProductIds, int companyId) throws Exception;

	List<MerchantProductSaleOffset> querySaleOffset(List<Long> merchantProductIds, int companyId) throws Exception;
}
