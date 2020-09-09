package com.odianyun.search.whale.index.business.process.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.odianyun.search.whale.data.model.BusinessProduct;
import com.odianyun.search.whale.processor.DataRecord;
import com.odianyun.search.whale.processor.Processor;
import com.odianyun.search.whale.processor.ProcessorContext;

public abstract class BaseProductPicUrlProcessor implements Processor {

	public abstract void calcProductPicUrl(Map<Long, BusinessProduct> map,int companyId) throws Exception;

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return BaseProductPicUrlProcessor.class.getSimpleName();
	}

	@Override
	public void process(ProcessorContext processorContext) throws Exception {
		List<DataRecord> dataRecords = processorContext.getDataRecords();
		Map<Long,BusinessProduct> map = new HashMap<Long,BusinessProduct>();
		for(DataRecord<BusinessProduct> dataRecord : dataRecords){
			BusinessProduct businessProduct = dataRecord.getV();
			map.put(businessProduct.getId(), businessProduct);
		}
		if(map.size() > 0){
			calcProductPicUrl(map,processorContext.getCompanyId());
		}
	}


}
