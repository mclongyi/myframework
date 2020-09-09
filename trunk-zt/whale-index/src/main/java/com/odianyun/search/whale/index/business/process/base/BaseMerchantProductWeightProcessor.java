package com.odianyun.search.whale.index.business.process.base;

import com.odianyun.search.whale.data.model.BusinessProduct;
import com.odianyun.search.whale.processor.DataRecord;
import com.odianyun.search.whale.processor.Processor;
import com.odianyun.search.whale.processor.ProcessorContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zengfenghua on 16/11/5.
 */
public class BaseMerchantProductWeightProcessor implements Processor {

    @Override
    public String getName() {
        return BaseMerchantProductWeightProcessor.class.getSimpleName();
    }

    @Override
    public void process(ProcessorContext processorContext) throws Exception {
        List<DataRecord> dataRecords = processorContext.getDataRecords();

        Map<Long, BusinessProduct> map = new HashMap<Long,BusinessProduct>();

        for (DataRecord<BusinessProduct> dataRecord : dataRecords) {
            BusinessProduct businessProduct = dataRecord.getV();
            map.put(businessProduct.getId(), businessProduct);
        }
        calcMerchantProductWeight(map,processorContext.getCompanyId());

    }

    public void calcMerchantProductWeight(Map<Long, BusinessProduct> businessProductMap,int companyId){

    }
}

