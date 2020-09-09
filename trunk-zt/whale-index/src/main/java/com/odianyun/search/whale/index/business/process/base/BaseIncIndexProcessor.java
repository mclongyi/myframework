package com.odianyun.search.whale.index.business.process.base;

import java.util.ArrayList;
import java.util.List;

import com.odianyun.search.whale.data.model.MerchantProductSearch;
import com.odianyun.search.whale.processor.DataRecord;
import com.odianyun.search.whale.processor.Processor;
import com.odianyun.search.whale.processor.ProcessorContext;


public abstract class BaseIncIndexProcessor implements Processor {

	public abstract void index(List<MerchantProductSearch> merchantProductSearchs, String indexName, String type, int companyId) throws Exception;

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return BaseIncIndexProcessor.class.getSimpleName();
	}

	@Override
	public void process(ProcessorContext processorContext) throws Exception {
		List<DataRecord> dataRecords = processorContext.getDataRecords();
		List<MerchantProductSearch> merchantProductSearchs = new ArrayList<MerchantProductSearch>();
		for(DataRecord<MerchantProductSearch> dataRecord : dataRecords){
			MerchantProductSearch mechantProductSearch = dataRecord.getV();
			merchantProductSearchs.add(mechantProductSearch);
		}
		index(merchantProductSearchs,processorContext.getIndexName(),processorContext.getIndexType(),processorContext.getCompanyId());
	}

}
