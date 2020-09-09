package com.odianyun.search.whale.data.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.odianyun.search.whale.data.model.MerchantProductStock;

public interface MerchantProductStockService {
	
	/**
	 * 从DB读取商品库存数据  mpid --> stockDetail
	 * @param merchantProductIds
	 * @return
	 */
	public Map<Long,List<MerchantProductStock>> getMerchantProductStocksDetailByTable(List<Long> merchantProductIds, int companyId) throws Exception;
	/**
	 * 从DB读取商品库存数据 mpid --> totalStock
	 * @param merchantProductIds
	 * @param companyId
	 * @return
	 * @throws Exception
	 */
	public Map<Long,Long> getMerchantProductTotalStocksByTable(List<Long> merchantProductIds, int companyId) throws Exception;
	/**
	 * 从DB读取商品库存数据 stock>0 的 mpid
	 * @param merchantProductIds
	 * @param companyId
	 * @return
	 * @throws Exception
	 */
	public Set<Long> getMerchantProductWithStocksByTable(List<Long> merchantProductIds, int companyId) throws Exception;

}
