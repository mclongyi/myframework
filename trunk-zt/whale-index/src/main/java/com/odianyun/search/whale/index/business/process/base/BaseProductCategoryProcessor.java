package com.odianyun.search.whale.index.business.process.base;

import java.util.ArrayList;
import java.util.List;

import com.odianyun.search.whale.data.model.BusinessProduct;
import com.odianyun.search.whale.processor.DataRecord;
import com.odianyun.search.whale.processor.Processor;
import com.odianyun.search.whale.processor.ProcessorContext;

public abstract class BaseProductCategoryProcessor implements Processor {

	public abstract void calcProductCategory(BusinessProduct businessProduct) throws Exception;

	public abstract void calcProductNaviCategory(List<BusinessProduct> businessProducts,Integer companyId) throws Exception;

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return BaseProductCategoryProcessor.class.getSimpleName();
	}

	@Override
	public void process(ProcessorContext processorContext) throws Exception {
		List<DataRecord> dataRecords = processorContext.getDataRecords();
		Integer companyId = processorContext.getCompanyId();
		List<BusinessProduct> businessProducts=new ArrayList<BusinessProduct>();
		for(DataRecord<BusinessProduct> dataRecord : dataRecords){
			BusinessProduct businessProduct = dataRecord.getV();
			businessProducts.add(businessProduct);
			calcProductCategory(businessProduct);
		}
		calcProductNaviCategory(businessProducts,companyId);
	}

}
