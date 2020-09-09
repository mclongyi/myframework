package com.odianyun.search.whale.index.business.process.base;

import com.odianyun.search.whale.data.model.BusinessProduct;
import com.odianyun.search.whale.index.scala.common.ProcessContext;
import com.odianyun.search.whale.processor.DataRecord;
import com.odianyun.search.whale.processor.Processor;
import com.odianyun.search.whale.processor.ProcessorContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Janz on 2017/3/20.
 */
public abstract class BaseMerchantProductRefIdProcess implements Processor{
    //单独处理refId的 在Relation中一次合并处理了
    public abstract void calcMerchantProductRefId(Map<Long, BusinessProduct> map, int companyId);
    public abstract void calcMerchantIdRelation(Map<Long, BusinessProduct> map, int companyId);

    @Override
    public String getName() {
        return BaseMerchantProductRefIdProcess.class.getSimpleName();
    }

    @Override
    public void process(ProcessorContext processorContext) throws Exception {
        List<DataRecord> dataRecords = processorContext.getDataRecords();
        Map<Long,BusinessProduct> map = new HashMap<Long,BusinessProduct>();
        for(DataRecord<BusinessProduct> dataRecord : dataRecords) {
            BusinessProduct businessProduct = dataRecord.getV();
            map.put(businessProduct.getId(),businessProduct);
        }
        if (map.size() > 0) {
            //calcMerchantProductRefId(map,processorContext.getCompanyId());
            calcMerchantIdRelation(map,processorContext.getCompanyId());
        }
    }


}
