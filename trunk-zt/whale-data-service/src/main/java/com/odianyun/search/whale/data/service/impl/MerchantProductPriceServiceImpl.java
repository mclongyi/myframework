package com.odianyun.search.whale.data.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.data.dao.MerchantProductPriceDao;
import com.odianyun.search.whale.data.model.MerchantProductPrice;
import com.odianyun.search.whale.data.service.MerchantProductPriceService;

public class MerchantProductPriceServiceImpl implements MerchantProductPriceService{
	
	@Autowired
	MerchantProductPriceDao merchantProductPriceDao;

	@Override
	public Map<Long,MerchantProductPrice> getMerchantProductPricesByTable(
			List<Long> merchantProductIds, int companyId) throws Exception{
		List<MerchantProductPrice> merchantProductPriceList= merchantProductPriceDao.queryMerchantProductPrice(merchantProductIds,companyId);
		List<MerchantProductPrice> merchantProductPromotionPriceList= merchantProductPriceDao.queryMerchantProductPromotionPrice(merchantProductIds,companyId);
		Map<Long,MerchantProductPrice> promotionPrices=new HashMap<>();
		if(CollectionUtils.isNotEmpty(merchantProductPromotionPriceList)){
			for(MerchantProductPrice merchantProductPrice:merchantProductPromotionPriceList){
				promotionPrices.put(merchantProductPrice.getMerchant_product_id(),merchantProductPrice);
			}
		}
		Map<Long,MerchantProductPrice> merchantProductPriceMap=new HashMap<Long,MerchantProductPrice>();
		for(MerchantProductPrice merchantProductPrice:merchantProductPriceList){
			merchantProductPriceMap.put(merchantProductPrice.getMerchant_product_id(), merchantProductPrice);
		}
		merchantProductPriceMap.putAll(promotionPrices);
		return merchantProductPriceMap;
	}

	@Override
	public Map<Long, Double> getMerchantProductPricesByIds(List<Long> merchantProductIds, int companyId) throws Exception{
		List<MerchantProductPrice> merchantProductPriceList= merchantProductPriceDao.queryMerchantProductPrice(merchantProductIds,companyId);
//		List<MerchantProductPrice> merchantProductPromotionPriceList= merchantProductPriceDao.queryMerchantProductPromotionPrice(merchantProductIds,companyId);
//        Map<Long,Double> promotionPrices=new HashMap<>();
//		if(CollectionUtils.isNotEmpty(merchantProductPromotionPriceList)){
//			for(MerchantProductPrice merchantProductPrice:merchantProductPromotionPriceList){
//				promotionPrices.put(merchantProductPrice.getMerchant_product_id(),merchantProductPrice.getMerchant_product_price());
//			}
//		}
		Map<Long,Double> merchantProductPriceMap=new HashMap<Long,Double>();
		for(MerchantProductPrice merchantProductPrice:merchantProductPriceList){
			merchantProductPriceMap.put(merchantProductPrice.getMerchant_product_id(), merchantProductPrice.getMerchant_product_price());
		}
//		merchantProductPriceMap.putAll(promotionPrices);
		return merchantProductPriceMap;	
	}

}
