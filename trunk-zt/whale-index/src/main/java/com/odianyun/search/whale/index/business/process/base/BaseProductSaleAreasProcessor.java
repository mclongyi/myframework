package com.odianyun.search.whale.index.business.process.base;

import com.odianyun.search.whale.data.model.BusinessProduct;
import com.odianyun.search.whale.processor.DataRecord;
import com.odianyun.search.whale.processor.Processor;
import com.odianyun.search.whale.processor.ProcessorContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/12/8.
 */
public abstract class BaseProductSaleAreasProcessor implements Processor {

    public abstract void calcProductSaleAreas(Map<Long, BusinessProduct> map, int companyId) throws Exception;

    @Override
    public String getName() {
        return BaseProductSaleAreasProcessor.class.getSimpleName();
    }

    @Override
    public void process(ProcessorContext processorContext) throws Exception {
        List<DataRecord> dataRecords = processorContext.getDataRecords();
        Map<Long,BusinessProduct> map = new HashMap<Long,BusinessProduct>();
        for(DataRecord<BusinessProduct> dataRecord : dataRecords){
            BusinessProduct businessProduct = dataRecord.getV();
            map.put(businessProduct.getId(),businessProduct);
        }
        if(map.size()>0){
            calcProductSaleAreas(map,processorContext.getCompanyId());
        }
    }


}
