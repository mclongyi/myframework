package com.odianyun.search.whale.index.business.process.base;

import com.odianyun.search.whale.data.model.BusinessProduct;
import com.odianyun.search.whale.processor.DataRecord;
import com.odianyun.search.whale.processor.Processor;
import com.odianyun.search.whale.processor.ProcessorContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zengfenghua on 17/5/16.
 */

/**
 * 分销相关的计算
 */
public abstract  class BaseDistributionProcessor implements Processor {

    public abstract void calcDistribution(Map<Long,BusinessProduct> map, int companyId) throws Exception;

    @Override
    public String getName() {
        return BaseDistributionProcessor.class.getSimpleName();
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
            calcDistribution(map,processorContext.getCompanyId());
        }
    }
}
