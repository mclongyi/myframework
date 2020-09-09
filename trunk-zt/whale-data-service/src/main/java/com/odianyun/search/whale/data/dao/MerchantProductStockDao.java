package com.odianyun.search.whale.data.dao;

import java.util.List;

import com.odianyun.search.whale.data.model.MerchantProductStock;

public interface MerchantProductStockDao {

//	public List<MerchantProductStock> queryAllMerchantProductStocks(int companyId) throws Exception;
	
	public List<MerchantProductStock> getMerchantProductStocks(List<Long> merchantProductIds, int companyId) throws Exception;
}
