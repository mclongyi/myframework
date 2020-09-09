package com.odianyun.search.whale.index.convert;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.odianyun.search.whale.data.model.MerchantProduct;

public class SeriesConverter extends SeriesMerchantProductConverter{

	public SeriesConverter(){
		
	}
	
	@Override
	public List<Long> convert(List<Long> ids, int companyId) throws Exception {
		List<Long> merchantProductIdList = new ArrayList<Long>();
		Set<Long> merchantProductIdSet = new HashSet<Long>();
		merchantProductIdSet.addAll(ids);
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
				merchantProductIdSet.addAll(super.convert(new ArrayList<Long>(merchantSeriesIdset), companyId));
			}
			if(CollectionUtils.isNotEmpty(merchantProductIdSet)){
				merchantProductIdList = new ArrayList<Long>(merchantProductIdSet);
			}
			
		}
		return merchantProductIdList;
	}

}
