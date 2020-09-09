package com.odianyun.search.whale.index.geo.build;

import com.odianyun.search.whale.index.geo.GeoProcessor;
import com.odianyun.search.whale.index.geo.GeoSegmentProcessor;
import com.odianyun.search.whale.index.geo.IncIndexProcessor;
import com.odianyun.search.whale.processor.Processor;
import com.odianyun.search.whale.processor.ProcessorsBuilder;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/11/29.
 */
public class GeoIncIndexProcessorBuilder implements ProcessorsBuilder {
    static IncIndexProcessor incIndexProcessor = new IncIndexProcessor();
    List<Processor> processors =new ArrayList<Processor>();
    static Logger logger = Logger.getLogger(GeoIncIndexProcessorBuilder.class);
    private List<IncIndexProcessor.IndexInfo> indexInfoList = new ArrayList<IncIndexProcessor.IndexInfo>();

    @Override
    public List<Processor> build() throws Exception {
        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(processors)){
            return processors;
        }
        synchronized (GeoIncIndexProcessorBuilder.class){
            if(org.apache.commons.collections.CollectionUtils.isNotEmpty(processors)){
                return processors;
            }
            processors.add(new GeoProcessor());
            processors.add(new GeoSegmentProcessor());
            processors.add(incIndexProcessor);
        }
        return processors;
    }

    public static void registe(String indexName,String indexType){
        IncIndexProcessor.IndexInfo indexInfo = new IncIndexProcessor.IndexInfo(indexName,indexType);
        incIndexProcessor.register(indexInfo);
        logger.info("GeoIndexProcessorBuilder register ( indexName : "+indexName+" indexType : "+indexType + ")");
        logger.info("GeoIncIndexProcessor indexInfo size : " + incIndexProcessor.indexInfoSize());
    }

    public static void remove(String indexName,String indexType){
        IncIndexProcessor.IndexInfo indexInfo = new IncIndexProcessor.IndexInfo(indexName,indexType);
        incIndexProcessor.remove(indexInfo);
        logger.info("GeoIndexProcessorBuilder remove ( indexName : "+indexName+" indexType : "+indexType + ")");
        logger.info("GeoIncIndexProcessor indexInfo size : " + incIndexProcessor.indexInfoSize());
    }

    public static List<IncIndexProcessor.IndexInfo> getIndexInfoList(){
        return incIndexProcessor.getIndexInfoList();
    }

    public static void clear(){
        incIndexProcessor.clear();
        logger.info("GeoIndexProcessorBuilder clear");
        logger.info("GeoIncIndexProcessor indexInfo size : " + incIndexProcessor.indexInfoSize());
    }
}
