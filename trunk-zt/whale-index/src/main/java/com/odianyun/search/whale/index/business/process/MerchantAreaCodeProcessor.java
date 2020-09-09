package com.odianyun.search.whale.index.business.process;

import java.util.List;

import com.odianyun.search.whale.data.model.BusinessProduct;
import com.odianyun.search.whale.data.model.MerchantBelongArea;
import com.odianyun.search.whale.data.service.AreaService;
import com.odianyun.search.whale.data.service.MerchantService;
import com.odianyun.search.whale.index.business.process.base.BaseMerchantAreaCodeProcessor;
import com.odianyun.search.whale.index.common.ProcessorApplication;

public class MerchantAreaCodeProcessor extends BaseMerchantAreaCodeProcessor {

	AreaService areaService;
	MerchantService merchantService;

	public MerchantAreaCodeProcessor(){
		areaService = (AreaService) ProcessorApplication.getBean("areaService");
		merchantService = (MerchantService) ProcessorApplication.getBean("merchantService");
	}
	@Override
	public void calcMerchantAreaCodes(BusinessProduct businessProduct)
			throws Exception {
		Long merchantId = businessProduct.getMerchantId();
		int companyId=businessProduct.getCompanyId().intValue();
		if(merchantId == null || merchantId == 0){
			return;
		}
		StringBuffer areaCodeStr = new StringBuffer();
		MerchantBelongArea belongArea = merchantService.getBelongAreaByMerchantId(merchantId,companyId);
		if(belongArea==null){
			return;
		}
		Long areaCode = belongArea.getArea_code();
		if(areaCode==null || areaCode.longValue()==0){
			return;
		}
		areaCodeStr.append(areaCode).append(" ");
		List<Long> parentCodes = areaService.getAllParentAreaCode(areaCode,companyId);
		if(parentCodes!=null){	
			for(Long code : parentCodes)
				areaCodeStr.append(code).append(" ");
		}
		
		businessProduct.setAreaCode(areaCodeStr.toString());
		
	}

}
