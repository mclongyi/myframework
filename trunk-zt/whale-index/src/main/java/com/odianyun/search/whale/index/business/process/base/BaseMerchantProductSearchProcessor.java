package com.odianyun.search.whale.index.business.process.base;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.odianyun.search.whale.data.model.BusinessProduct;
import com.odianyun.search.whale.data.model.MerchantProductSearch;
import com.odianyun.search.whale.processor.DataRecord;
import com.odianyun.search.whale.processor.Processor;
import com.odianyun.search.whale.processor.ProcessorContext;

public abstract class BaseMerchantProductSearchProcessor implements Processor {

	public abstract MerchantProductSearch convert(BusinessProduct businessProduct,String updateTime) throws Exception;
	private static SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

	@Override
	public String getName() {
		return BaseMerchantProductSearchProcessor.class.getSimpleName();
	}

	@Override
	public void process(ProcessorContext processorContext) throws Exception {
		List<DataRecord> dataRecords = processorContext.getDataRecords();
		List<DataRecord> newDataRecords = new ArrayList<DataRecord>();
		String indexName = processorContext.getIndexName();
		String updateTime = null;
//		if(indexName.endsWith(ServiceConstants.ALIAS)){
		if(indexName.length() < 19){
			updateTime = simpleDateFormat.format(new Date());
		}else{
			//"yyyy-MM-dd_HH-mm-ss".length()=19
			updateTime = indexName.substring(indexName.length() - 19);
		}
		for(DataRecord<BusinessProduct> dataRecord : dataRecords){
			BusinessProduct businessProduct = dataRecord.getV();
			MerchantProductSearch merchantProductSearch = convert(businessProduct,updateTime);
			if(businessProduct != null ){
				newDataRecords.add(new DataRecord<MerchantProductSearch>(merchantProductSearch));
			}
		}
		processorContext.setDataRecords(newDataRecords);
	}

}
