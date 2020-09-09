package com.odianyun.search.whale.index.business.process.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.odianyun.search.whale.data.model.BusinessProduct;
import com.odianyun.search.whale.processor.DataRecord;
import com.odianyun.search.whale.processor.Processor;
import com.odianyun.search.whale.processor.ProcessorContext;


/**
 * 
 * @author godspeed
 *
 */
public class BaseMerchantVolume4SaleProcessor implements Processor{
	
	public void calcMerchantVolume4Sale(Map<Long,BusinessProduct> map,int companyId,String indexName) throws Exception {
	}

	public String getName() {
		// TODO Auto-generated method stub
		return BaseMerchantVolume4SaleProcessor.class.getSimpleName();
	}

	public void process(ProcessorContext processorContext) throws Exception {
		// TODO Auto-generated method stub
		//获取传递的泛型参数的类型
				List<DataRecord> dataRecords = processorContext.getDataRecords();
				
				Map<Long, BusinessProduct> map = new HashMap<Long,BusinessProduct>();
				
				for (DataRecord<BusinessProduct> dataRecord : dataRecords) {
					 BusinessProduct businessProduct = dataRecord.getV();
					 map.put(businessProduct.getId(), businessProduct);
				}
				
				if (map.size() > 0) {
					calcMerchantVolume4Sale(map,processorContext.getCompanyId(),processorContext.getIndexName());
				}
		
	}
	


	
	
	
}
