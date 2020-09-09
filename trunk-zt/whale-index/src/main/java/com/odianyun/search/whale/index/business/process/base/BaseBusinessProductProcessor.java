package com.odianyun.search.whale.index.business.process.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.odianyun.search.whale.data.model.BusinessProduct;
import com.odianyun.search.whale.data.model.MerchantProduct;
import com.odianyun.search.whale.processor.DataRecord;
import com.odianyun.search.whale.processor.Processor;
import com.odianyun.search.whale.processor.ProcessorContext;


public abstract class BaseBusinessProductProcessor implements Processor{

	public abstract BusinessProduct convert(MerchantProduct merchantProduct) throws Exception;
	
	public abstract void calcProduct(Map<Long, BusinessProduct> map) throws Exception;
	
	//外卖 ProductMerchantProcessor 迁移到此
	public abstract void calcProductMerchant(BusinessProduct businessProduct) throws Exception;

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return BaseBusinessProductProcessor.class.getSimpleName();
	}

	@Override
	public void process(ProcessorContext processorContext) throws Exception {
		List<DataRecord> dataRecords = processorContext.getDataRecords();
		List<DataRecord> newDataRecords = new ArrayList<DataRecord>();
		Map<Long,BusinessProduct> map = new HashMap<Long,BusinessProduct>();

		for(DataRecord<MerchantProduct> dataRecord : dataRecords){
			MerchantProduct merchantProduct = dataRecord.getV();
			BusinessProduct businessProduct = convert(merchantProduct);
			if(businessProduct != null ){
				calcProductMerchant(businessProduct);
				newDataRecords.add(new DataRecord<BusinessProduct>(businessProduct));
				map.put(businessProduct.getId(), businessProduct);
			}
		}
		if(map.size() > 0){
			calcProduct(map);
		}
		processorContext.setDataRecords(newDataRecords);		
	}

}
