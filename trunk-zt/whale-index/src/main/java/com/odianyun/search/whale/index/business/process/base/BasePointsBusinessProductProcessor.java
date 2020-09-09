package com.odianyun.search.whale.index.business.process.base;

import com.odianyun.search.whale.data.model.BusinessProduct;
import com.odianyun.search.whale.processor.DataRecord;
import com.odianyun.search.whale.processor.Processor;
import com.odianyun.search.whale.processor.ProcessorContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class BasePointsBusinessProductProcessor implements Processor{

	@Override
	public String getName() {
		return BasePointsBusinessProductProcessor.class.getSimpleName();
	}

	@Override
	public void process(ProcessorContext processorContext) throws Exception {
		List<DataRecord> dataRecords = processorContext.getDataRecords();
		Map<Long,BusinessProduct> map = new HashMap<Long,BusinessProduct>();

		for(DataRecord<BusinessProduct> dataRecord : dataRecords){
			BusinessProduct businessProduct = dataRecord.getV();
			if(businessProduct != null ){
				map.put(businessProduct.getId(),businessProduct);
			}
		}
		if(map.size() > 0){
			calcPointsMallProduct(map,processorContext);
		}

	}

	protected abstract void calcPointsMallProduct(Map<Long, BusinessProduct> map,ProcessorContext processorContext) throws Exception;

}
