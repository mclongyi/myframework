package com.odianyun.search.whale.index.business.process.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.odianyun.search.whale.data.model.BusinessProduct;
import com.odianyun.search.whale.processor.DataRecord;
import com.odianyun.search.whale.processor.Processor;
import com.odianyun.search.whale.processor.ProcessorContext;

public abstract class BaseProductSeriesProcessor implements Processor {

	/*public abstract void calcSeriesType(Map<Long,BusinessProduct> businessProduct,
			List<Long> merchantSeriesIds,int companyId) throws Exception;*/

	public abstract void calcSeriesType(Map<Long,BusinessProduct> businessProduct,
			Map<Long,List<Long>> seriesMpMap,int companyId) throws Exception;
	
	@Override
	public String getName() {
		return BaseProductSeriesProcessor.class.getSimpleName();
	}

	@Override
	public void process(ProcessorContext processorContext) throws Exception {
		List<DataRecord> dataRecords = processorContext.getDataRecords();
		Map<Long,BusinessProduct> seriesBusMap = new HashMap<Long,BusinessProduct>();
//		Set<Long> merchantSeriesIdset =new HashSet<Long>();
		Map<Long,List<Long>> seriesMpMap = new HashMap<Long,List<Long>>();
		for(DataRecord<BusinessProduct> dataRecord : dataRecords){
			BusinessProduct businessProduct = dataRecord.getV();
			Long merchantSeriesId = businessProduct.getMerchantSeriesId();
			if(merchantSeriesId==null||merchantSeriesId==0)
				continue;
			seriesBusMap.put(businessProduct.getId(), businessProduct);
			List<Long> mpIdList = seriesMpMap.get(merchantSeriesId);
			if(mpIdList == null){
				mpIdList = new ArrayList<Long>();
				seriesMpMap.put(merchantSeriesId, mpIdList);
			}
			mpIdList.add(businessProduct.getId());
//			merchantSeriesIdset.add(merchantSeriesId);
		}
//		calcSeriesType(seriesBusMap,new ArrayList<Long>(merchantSeriesIdset),processorContext.getCompanyId());
		calcSeriesType(seriesBusMap,seriesMpMap,processorContext.getCompanyId());

	}

}
