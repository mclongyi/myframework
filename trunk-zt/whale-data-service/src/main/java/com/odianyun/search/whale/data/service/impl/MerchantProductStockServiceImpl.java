package com.odianyun.search.whale.data.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.data.dao.MerchantProductStockDao;
import com.odianyun.search.whale.data.model.MerchantProductStock;
import com.odianyun.search.whale.data.service.MerchantProductStockService;

public class MerchantProductStockServiceImpl implements
		MerchantProductStockService {

	@Autowired
	MerchantProductStockDao stockDao;

	@Override
	public Map<Long, List<MerchantProductStock>> getMerchantProductStocksDetailByTable(
			List<Long> merchantProductIds, int companyId) throws Exception{
		List<MerchantProductStock> stocks = stockDao.getMerchantProductStocks(merchantProductIds,companyId);
		Map<Long,List<MerchantProductStock>> ret =new HashMap<Long,List<MerchantProductStock>>();
		if(CollectionUtils.isNotEmpty(stocks)){
			for(MerchantProductStock mps:stocks){
				List<MerchantProductStock> stockList=ret.get(mps.getMerchant_product_id());
				if(stockList==null){
					stockList=new ArrayList<MerchantProductStock>();
					ret.put(mps.getMerchant_product_id(), stockList);
				}
				stockList.add(mps);
			}
		}
		return ret;
	}

	@Override
	public Map<Long, Long> getMerchantProductTotalStocksByTable(List<Long> merchantProductIds, int companyId)
			throws Exception {
		List<MerchantProductStock> stocks = stockDao.getMerchantProductStocks(merchantProductIds,companyId);
		Map<Long,Long> ret = new HashMap<Long,Long>();
		if(CollectionUtils.isNotEmpty(stocks)){
			for(MerchantProductStock mpStock : stocks){
				long num = mpStock.getReal_stock_num() - mpStock.getReal_frozen_stock_num();
				Long stock = ret.get(mpStock.getMerchant_product_id());
				if(null == stock){
					stock = 0l;
				}
				ret.put(mpStock.getMerchant_product_id(), stock + num);
			}
		}
		return ret;
	}

	@Override
	public Set<Long> getMerchantProductWithStocksByTable(List<Long> merchantProductIds, int companyId) throws Exception {
		List<MerchantProductStock> stocks = stockDao.getMerchantProductStocks(merchantProductIds,companyId);
		Set<Long> ret = new HashSet<Long>();
		if(CollectionUtils.isNotEmpty(stocks)){
			for(MerchantProductStock mpStock : stocks){
				long num = mpStock.getReal_stock_num() - mpStock.getReal_frozen_stock_num();
				if(num > 0){
					ret.add(mpStock.getMerchant_product_id());
				}
			}
		}
		return ret;
	}

}
