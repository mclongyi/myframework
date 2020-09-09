package com.odianyun.search.whale.index.convert;

import com.odianyun.search.whale.data.model.MerchantProduct;
import com.odianyun.search.whale.data.service.MerchantProductService;
import com.odianyun.search.whale.data.service.PointsMallProductService;
import com.odianyun.search.whale.index.common.ProcessorApplication;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;

public class PointsMallProductConverter extends SeriesConverter{

	PointsMallProductService pointsMallProductService;
	public PointsMallProductConverter(){
		pointsMallProductService = (PointsMallProductService) ProcessorApplication.getBean("pointsMallProductService");

	}
	
	@Override
	public List<Long> convert(List<Long> ids, int companyId) throws Exception {
		List<Long> merchantProductIdList = pointsMallProductService.getMpIdListByPointsMallProductId(ids,companyId);
		if(CollectionUtils.isNotEmpty(merchantProductIdList)){
			merchantProductIdList = super.convert(merchantProductIdList,companyId);
		}
		return merchantProductIdList;
	}

}
