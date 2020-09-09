package com.odianyun.search.whale.index.business.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.odianyun.search.whale.data.model.BusinessProduct;
import com.odianyun.search.whale.data.service.MerchantProductService;
import com.odianyun.search.whale.index.business.process.base.BaseProductPicUrlProcessor;
import com.odianyun.search.whale.index.common.O2OConstants;
import com.odianyun.search.whale.index.common.ProcessorApplication;

public class ProductPicUrlProcessor extends BaseProductPicUrlProcessor{
	
	MerchantProductService merchantProductService;
	
	public ProductPicUrlProcessor(){
		merchantProductService = (MerchantProductService) ProcessorApplication.getBean("merchantProductService");
	}
	@Override
	public void calcProductPicUrl(Map<Long, BusinessProduct> map,int companyId) throws Exception {
		//外卖修改 start
		List<Long> merchantProductIdsO2O = null;
		List<Long> merchantProductIds = null;
		for(Map.Entry<Long, BusinessProduct> entry : map.entrySet()){
			Long merchantProductId = entry.getKey();
			BusinessProduct businessProduct = entry.getValue();
			if(O2OConstants.MemberType_O2O.equals(businessProduct.getMerchantType())) {
				if(merchantProductIdsO2O == null) {
					merchantProductIdsO2O = new ArrayList<Long>();
				}
				Long seriesParentId;
				if(businessProduct.getSeriesParentId() == null || businessProduct.getSeriesParentId() == 0L) {
					seriesParentId = merchantProductId;
				}else {
					seriesParentId = businessProduct.getSeriesParentId();
				}
				merchantProductIdsO2O.add(seriesParentId);
			}else {
				if(merchantProductIds == null) {
					merchantProductIds = new ArrayList<Long>();
				}
				merchantProductIds.add(merchantProductId);
			}
		}
		Map<Long,String> urlsO2OMap = null;
		if(merchantProductIdsO2O != null && merchantProductIdsO2O.size() > 0) {
			urlsO2OMap = merchantProductService.getMerchantProductUrls(merchantProductIdsO2O,companyId);
		}
		if(urlsO2OMap == null || urlsO2OMap.size() == 0) {
			urlsO2OMap = new HashMap<Long,String>();
		}
		Map<Long,String> urlsMap = null;
		if(merchantProductIds != null && merchantProductIds.size() > 0) {
			urlsMap = merchantProductService.getMerchantProductUrls(merchantProductIds,companyId);
		}
		
		if(urlsMap == null || urlsMap.size() == 0){
			urlsMap = new HashMap<Long,String>();
		}
		for(Map.Entry<Long, BusinessProduct> entry : map.entrySet()){
			Long merchantProductId = entry.getKey();
			BusinessProduct businessProduct = entry.getValue();
			String url = "";
			if(O2OConstants.MemberType_O2O.equals(businessProduct.getMerchantType())) {
				url = urlsO2OMap.get(businessProduct.getSeriesParentId());
			}else {
				url = urlsMap.get(merchantProductId);
			}
			if(StringUtils.isBlank(url)){
				businessProduct.setHasPic(0);
			}else {
				businessProduct.setPicUrl(url);
			}
		}
		//外卖修改 end
	}

}
