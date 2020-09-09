package com.odianyun.search.whale.index.geo;

import com.odianyun.search.whale.data.model.BusinessProduct;
import com.odianyun.search.whale.data.model.geo.O2OStore;
import com.odianyun.search.whale.processor.DataRecord;
import com.odianyun.search.whale.processor.Processor;
import com.odianyun.search.whale.processor.ProcessorContext;

import java.util.List;

/**
 * Created by admin on 2016/11/25.
 */
public abstract class GeoBaseSegmentProcessor implements Processor {

    public abstract void dosegment(O2OStore o2OStore) throws Exception;
    @Override
    public String getName() {
        return GeoBaseSegmentProcessor.class.getSimpleName();
    }

    @Override
    public void process(ProcessorContext processorContext) throws Exception {
        List<DataRecord> dataRecords = processorContext.getDataRecords();
        for(DataRecord<O2OStore> dataRecord : dataRecords){
            O2OStore o2OStore = dataRecord.getV();
            if(o2OStore != null){
                dosegment(o2OStore);
            }
        }
    }
}
