package com.odianyun.search.whale.index.business.process.base;

import java.util.Iterator;
import java.util.List;

import com.odianyun.search.whale.common.util.ConfigUtil;
import com.odianyun.search.whale.data.model.BusinessProduct;
import com.odianyun.search.whale.data.service.ConfigService;
import com.odianyun.search.whale.index.common.ProcessorApplication;
import com.odianyun.search.whale.processor.DataRecord;
import com.odianyun.search.whale.processor.Processor;
import com.odianyun.search.whale.processor.ProcessorContext;

public abstract class BaseProductMerchantProcessor implements Processor {
	
	boolean IS_MERCHANT_FILTER = false;
	
	ConfigService configService;
	
	public BaseProductMerchantProcessor(){
		configService = (ConfigService) ProcessorApplication.getBean("configService");
	}
	
	public abstract void calcProductMerchant(BusinessProduct businessProduct) throws Exception;

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return BaseProductMerchantProcessor.class.getSimpleName();
	}

	@Override
	public void process(ProcessorContext processorContext) throws Exception {
		IS_MERCHANT_FILTER = configService.getBool("is_merchant_filter",false,processorContext.getCompanyId());
		List<DataRecord> dataRecords = processorContext.getDataRecords();
		Iterator<DataRecord> iter=dataRecords.iterator();
		while(iter.hasNext()){
			DataRecord<BusinessProduct> dataRecord=iter.next();
			BusinessProduct businessProduct = dataRecord.getV();
			// calcProductMerchant(businessProduct); 外卖合并到 BusinessProductProcessor
			if(IS_MERCHANT_FILTER && !businessProduct.isMerchant_status()){
				iter.remove();
			}
		}
	}

}
