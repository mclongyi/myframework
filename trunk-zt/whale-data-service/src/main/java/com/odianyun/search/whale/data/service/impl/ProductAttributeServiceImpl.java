package com.odianyun.search.whale.data.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.odianyun.search.whale.data.model.MerchantProdAttValue;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.data.dao.ProductAttributeDao;
import com.odianyun.search.whale.data.model.MerchantProductAttributeValue;
import com.odianyun.search.whale.data.model.ProductAttributeValue;
import com.odianyun.search.whale.data.service.ProductAttributeService;

public class ProductAttributeServiceImpl implements ProductAttributeService{
	
	@Autowired
	ProductAttributeDao productAttributeDao;
	
	/*Map<Long,List<ProductAttributeValue>> pavs;

	@Override
	public void reload() throws Exception {
		Map<Long,List<ProductAttributeValue>> productAttributeValues=new HashMap<Long, List<ProductAttributeValue>>();
		List<ProductAttributeValue> prodAttributeValues=productAttributeDao.queryAllProductAttributeValues();
		for(ProductAttributeValue pav:prodAttributeValues){
			List<ProductAttributeValue> pavList=productAttributeValues.get(pav.getProductId());
			if(pavList==null){
				pavList=new ArrayList<ProductAttributeValue>();
				productAttributeValues.put(pav.getProductId(), pavList);
			}
			pavList.add(pav);
		}
		this.pavs=productAttributeValues;
	}

	@Override
	public List<ProductAttributeValue> queryProductAttributeValues(
			Long productId) {
		return pavs.get(productId);
	}*/

	@Override
	public Map<Long,List<ProductAttributeValue>> queryProductAttributeValuesTable(List<Long> productIds, int companyId) throws Exception {
		List<ProductAttributeValue> pavs=productAttributeDao.queryProductAttributeValues(productIds,companyId);
		Map<Long,List<ProductAttributeValue>> pavMap=new HashMap<Long,List<ProductAttributeValue>>();
		if(pavs!=null){
			for(ProductAttributeValue pav:pavs){
				List<ProductAttributeValue> pavList=pavMap.get(pav.getProductId());
				if(pavList==null){
					pavList=new ArrayList<ProductAttributeValue>();
					pavMap.put(pav.getProductId(), pavList);
				}
				pavList.add(pav);
			}
		}
		return pavMap;
	}

	@Override
	public Map<Long, List<MerchantProductAttributeValue>> queryMerchantProductAttributeValuesByTable(
			List<Long> merchantProductIds, int companyId) throws Exception {
		Map<Long, List<MerchantProductAttributeValue>> mpavMap=new HashMap<Long, List<MerchantProductAttributeValue>>();
		List<MerchantProductAttributeValue> mpavList=productAttributeDao.queryMerchantProductAttributeValues(merchantProductIds,companyId);
		if(CollectionUtils.isNotEmpty(mpavList)){
			for(MerchantProductAttributeValue mpav:mpavList){
				List<MerchantProductAttributeValue> mpavs=mpavMap.get(mpav.getMerchantProductId());
				if(mpavs==null){
					mpavs=new ArrayList<MerchantProductAttributeValue>();
					mpavMap.put(mpav.getMerchantProductId(), mpavs);
				}
				mpavs.add(mpav);
			}
		}
		return mpavMap;
	}

	@Override
	public Map<Long, Map<Long, String>> queryMerchantProductAttributeValueCustom(List<Long> merchantProductIds, int companyId) throws Exception {
		Map<Long, Map<Long, String>> mpanvMap = new HashMap<>();
		List<MerchantProdAttValue> mpanvList = productAttributeDao.queryMerchantProdAttValues(merchantProductIds, companyId);
		if(CollectionUtils.isNotEmpty(mpanvList)){
			for(MerchantProdAttValue mpav : mpanvList){
				Map<Long, String> mpavs=mpanvMap.get(mpav.getMerchantProductId());
				if(mpavs==null){
					mpavs=new HashMap<>();
					mpanvMap.put(mpav.getMerchantProductId(), mpavs);
				}
				mpavs.put(mpav.getAttValueId(), mpav.getAttValueCustom());
			}
		}
		return mpanvMap;
	}

}
