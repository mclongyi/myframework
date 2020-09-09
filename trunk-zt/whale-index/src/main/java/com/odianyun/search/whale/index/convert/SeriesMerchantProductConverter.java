package com.odianyun.search.whale.index.convert;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.odianyun.search.whale.data.model.MerchantProduct;
import com.odianyun.search.whale.data.service.MerchantProductService;
import com.odianyun.search.whale.data.service.MerchantSeriesService;
import com.odianyun.search.whale.index.common.ProcessorApplication;

public class SeriesMerchantProductConverter implements IDConverter {

	MerchantSeriesService seriesService;
	MerchantProductService merchantProductService;

	public SeriesMerchantProductConverter(){
		seriesService = (MerchantSeriesService) ProcessorApplication.getBean("seriesService");
		merchantProductService = (MerchantProductService) ProcessorApplication.getBean("merchantProductService");

	}
	
	@Override
	public List<Long> convert(List<Long> ids, int companyId) throws Exception {
		return seriesService.getSeriesMerchantProductIds(ids,companyId);
	}
	/*@Override
	public List<Long> convert(List<Long> ids, int companyId) throws Exception {
		Set<Long> merchantProductIdSet = new HashSet<Long>(ids);
		Map<Long,MerchantProduct> merchantProductMap = merchantProductService.getMerchantProductsAll(ids, companyId);
		if(null != merchantProductMap && merchantProductMap.size() > 0){
			Set<Long> merchantSeriesIdset =new HashSet<Long>();

			for(Map.Entry<Long, MerchantProduct> entry : merchantProductMap.entrySet()){
				MerchantProduct merchantProduct = entry.getValue();
				if(null != merchantProduct){
					Long merchantSeriesId = merchantProduct.getMerchantSeriesId();
					if(merchantSeriesId==null||merchantSeriesId==0){
						continue;
					}
					merchantSeriesIdset.add(merchantSeriesId);
				}
			}
			if(CollectionUtils.isNotEmpty(merchantSeriesIdset)){
				List<Long> mpIdList = seriesService.getSeriesMerchantProductIds(new ArrayList<Long>(merchantSeriesIdset),companyId);
				if(CollectionUtils.isNotEmpty(mpIdList)){
					merchantProductIdSet.addAll(mpIdList);
				}
			}
		}
		return new ArrayList<Long>(merchantProductIdSet);
	}*/

}
