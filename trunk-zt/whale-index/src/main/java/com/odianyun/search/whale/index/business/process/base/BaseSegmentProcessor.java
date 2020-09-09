package com.odianyun.search.whale.index.business.process.base;

import java.util.List;

import com.odianyun.search.whale.data.model.BusinessProduct;
import com.odianyun.search.whale.processor.DataRecord;
import com.odianyun.search.whale.processor.Processor;
import com.odianyun.search.whale.processor.ProcessorContext;

public abstract class BaseSegmentProcessor implements Processor {
	
	public abstract void dosegment(BusinessProduct businessProduct) throws Exception;

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return BaseSegmentProcessor.class.getSimpleName();
	}

	@Override
	public void process(ProcessorContext processorContext) throws Exception {
		// TODO Auto-generated method stub
		List<DataRecord> dataRecords = processorContext.getDataRecords();
		for(DataRecord<BusinessProduct> dataRecord : dataRecords){
			BusinessProduct businessProduct = dataRecord.getV();
			if(businessProduct != null){
				dosegment(businessProduct);
			}
		}
	}


}
