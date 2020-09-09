package com.odianyun.search.whale.index.business.process.base;

import java.util.List;

import com.odianyun.search.whale.data.model.BusinessProduct;
import com.odianyun.search.whale.processor.DataRecord;
import com.odianyun.search.whale.processor.Processor;
import com.odianyun.search.whale.processor.ProcessorContext;

public abstract class BaseMerchantAreaCodeProcessor implements Processor {

	public abstract void calcMerchantAreaCodes(BusinessProduct businessProduct) throws Exception;
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return BaseMerchantAreaCodeProcessor.class.getSimpleName();
	}

	@Override
	public void process(ProcessorContext processorContext) throws Exception {
		List<DataRecord> dataRecords = processorContext.getDataRecords();
		for(DataRecord<BusinessProduct> dataRecord : dataRecords){
			BusinessProduct businessProduct = dataRecord.getV();
			calcMerchantAreaCodes(businessProduct);
		}
	}

}
