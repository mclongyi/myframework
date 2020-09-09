package com.odianyun.search.whale.index.convert;

import com.odianyun.search.whale.data.service.MerchantProductService;
import com.odianyun.search.whale.data.service.MerchantSeriesService;
import com.odianyun.search.whale.data.service.ProductSeriesService;
import com.odianyun.search.whale.index.common.ProcessorApplication;

import java.util.List;

public class ProductSeriesMerchantProductConverter implements IDConverter {

	ProductSeriesService productSeriesService;
	MerchantProductService merchantProductService;

	public ProductSeriesMerchantProductConverter(){
		productSeriesService = (ProductSeriesService) ProcessorApplication.getBean("productSeriesService");
		merchantProductService = (MerchantProductService) ProcessorApplication.getBean("merchantProductService");

	}
	
	@Override
	public List<Long> convert(List<Long> ids, int companyId) throws Exception {
		return productSeriesService.getSeriesMerchantProductIds(ids,companyId);
	}


}
