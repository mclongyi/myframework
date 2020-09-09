package com.odianyun.search.whale.data.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.odianyun.common.utils.string.StringUtil;
import com.odianyun.search.whale.data.model.MerchantProductSaleOffset;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.data.dao.Volume4SaleDao;
import com.odianyun.search.whale.data.model.MerchantProductVolume4Sale;
import com.odianyun.search.whale.data.model.Volume4Sale;
import com.odianyun.search.whale.data.service.Volume4SaleService;

public class Volume4SaleServiceImpl implements Volume4SaleService{

	@Autowired
	Volume4SaleDao volume4SaleDao;
	
	/**
	 *  完成商品ID 和商品销售数量的一一对应的处理;
	 */
	@Override
	public Map<Long, Long> getMerchantVolume4SaleByIds(
			List<Long> merchantProductIds, int companyId) throws Exception {
		List<MerchantProductVolume4Sale> mpVolume4SaleList = volume4SaleDao.queryVolume4Sale(merchantProductIds,companyId);
		List<MerchantProductSaleOffset> mpSaleOffsetList = volume4SaleDao.querySaleOffset(merchantProductIds,companyId);
		Map<Long,Long> mpVolume4SaleMap = new HashMap<Long,Long>();
		Map<Long,Long> mpSaleOffsetMap = new HashMap<>();

		//将偏移量和商品id转换成map形式
		for(MerchantProductSaleOffset mpOffset : mpSaleOffsetList){
			mpSaleOffsetMap.put(mpOffset.getMpId(), mpOffset.getOffset());
		}
		//将商品Id和商品销售数量变成map的形式
		for (MerchantProductVolume4Sale MPVolume4Sale : mpVolume4SaleList) {
			mpVolume4SaleMap.put(MPVolume4Sale.getMerchant_product_id(), Long.valueOf(MPVolume4Sale.getVolume4sales()));
		}
		Long sale;
		for(Long mpId : merchantProductIds){
			sale = 0L;
			if(mpVolume4SaleMap.containsKey(mpId)){
				sale = mpVolume4SaleMap.get(mpId);
			}
			if(mpSaleOffsetMap.containsKey(mpId)){
				sale = sale + mpSaleOffsetMap.get(mpId);
			}
			mpVolume4SaleMap.put(mpId,sale);
		}
		/*for(Map.Entry<Long,Long> enty : mpSaleOffsetMap.entrySet()){
			if(mpVolume4SaleMap.containsKey(enty.getKey())){
				mpVolume4SaleMap.put(enty.getKey(), Long.valueOf(enty.getValue())+mpVolume4SaleMap.get(enty.getKey()));
			}else{
				mpVolume4SaleMap.put(enty.getKey(),enty.getValue());
			}
		}*/
		
		return mpVolume4SaleMap;
	}

	@Override
	public Map<Long, Volume4Sale> getMerchantVolume4SaleDetailByIds(List<Long> merchantProductIds, int companyId)
			throws Exception {
		Map<Long, Volume4Sale> resultMap = new HashMap<Long, Volume4Sale>();
		List<MerchantProductVolume4Sale> mpVolume4SaleList = volume4SaleDao.queryVolume4Sale(merchantProductIds,companyId);
		List<MerchantProductSaleOffset> mpSaleOffsetList = volume4SaleDao.querySaleOffset(merchantProductIds,companyId);
		Map<Long,Long> mpVolume4SaleMap = new HashMap<Long,Long>();
		Map<Long,Long> mpSaleOffsetMap = new HashMap<>();

		//将偏移量和商品id转换成map形式
		for(MerchantProductSaleOffset mpOffset : mpSaleOffsetList){
			mpSaleOffsetMap.put(mpOffset.getMpId(), mpOffset.getOffset());
		}
		//将商品Id和商品销售数量变成map的形式
		for (MerchantProductVolume4Sale MPVolume4Sale : mpVolume4SaleList) {
			mpVolume4SaleMap.put(MPVolume4Sale.getMerchant_product_id(), Long.valueOf(MPVolume4Sale.getVolume4sales()));
		}
		Long sale;
		for(Long mpId : merchantProductIds){
			Volume4Sale volume4Sale = new Volume4Sale();
			sale = 0L;
			if(mpVolume4SaleMap.containsKey(mpId)){
				sale = mpVolume4SaleMap.get(mpId);
			}
			volume4Sale.setRealSale(sale);
			if(mpSaleOffsetMap.containsKey(mpId)){
				sale = sale + mpSaleOffsetMap.get(mpId);
			}
			volume4Sale.setSale(sale);
			resultMap.put(mpId,volume4Sale);
		}
		
		return resultMap;
	}
	
	
}
