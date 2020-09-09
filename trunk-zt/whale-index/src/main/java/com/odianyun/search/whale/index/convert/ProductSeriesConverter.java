package com.odianyun.search.whale.index.convert;

import com.odianyun.search.whale.data.model.MerchantProduct;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;

public class ProductSeriesConverter extends ProductSeriesMerchantProductConverter{

	public ProductSeriesConverter(){
		
	}
	
	@Override
	public List<Long> convert(List<Long> ids, int companyId) throws Exception {
		List<Long> merchantProductIdList = new ArrayList<Long>();
		Set<Long> merchantProductIdSet = new HashSet<Long>();
		merchantProductIdSet.addAll(ids);
		Map<Long,MerchantProduct> merchantProductMap = merchantProductService.getMerchantProductsAll(ids, companyId);
		if(null != merchantProductMap && merchantProductMap.size() > 0){
			Set<Long> seriesParentIdSet =new HashSet<Long>();

			for(Map.Entry<Long, MerchantProduct> entry : merchantProductMap.entrySet()){
				MerchantProduct merchantProduct = entry.getValue();
				if(null != merchantProduct){
					Long seriesParentId = merchantProduct.getSeriesParentId();
					if(seriesParentId==null||seriesParentId==0){
						continue;
					}
					seriesParentIdSet.add(seriesParentId);
				}
			}
			
			if(CollectionUtils.isNotEmpty(seriesParentIdSet)){
				merchantProductIdSet.addAll(super.convert(new ArrayList<Long>(seriesParentIdSet), companyId));
			}
			if(CollectionUtils.isNotEmpty(merchantProductIdSet)){
				merchantProductIdList = new ArrayList<Long>(merchantProductIdSet);
			}
			
		}
		return merchantProductIdList;
	}

}
