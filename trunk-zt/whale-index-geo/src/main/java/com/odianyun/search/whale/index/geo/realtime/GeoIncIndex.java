package com.odianyun.search.whale.index.geo.realtime;


import com.odianyun.search.whale.common.util.GsonUtil;
import com.odianyun.search.whale.data.model.Merchant;
import com.odianyun.search.whale.data.service.MerchantService;
import com.odianyun.search.whale.es.api.ESClient;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.index.geo.GeoProcessor;
import com.odianyun.search.whale.index.geo.GeoSegmentProcessor;
import com.odianyun.search.whale.index.geo.IncIndexProcessor;
import com.odianyun.search.whale.index.geo.IndexUpdater;
import com.odianyun.search.whale.index.geo.build.GeoIncIndexProcessorBuilder;
import com.odianyun.search.whale.processor.DataRecord;
import com.odianyun.search.whale.processor.Processor;
import com.odianyun.search.whale.processor.ProcessorContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.client.Client;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jzz on 2016/11/28.
 */
public class GeoIncIndex {
    static Logger logger = Logger.getLogger(GeoIncIndex.class);

    MerchantService merchantService;

    public String process(List<Long> merchantIds, boolean isSendIndexRequest, String indexName,
                          String type, int companyId) throws Exception{
        List<Merchant> updateMerchantList = new  ArrayList<Merchant>();
        if(merchantIds == null || merchantIds.size() == 0){
            return "merchantIds can not be null ";
        }
        if(isSendIndexRequest){
            List<Long> updateMerchantIds;
            Map<Long,Merchant> merchantMap;
            List<Processor> processors = new GeoIncIndexProcessorBuilder().build();
            merchantMap = merchantService.getMerchantsByIds(merchantIds,companyId);
            /*if(merchantMap == null || merchantMap.size() == 0){
                return "";
            }*/
            updateMerchantIds = new ArrayList<Long>(merchantMap.keySet());
            updateMerchantList = new ArrayList<Merchant>(merchantMap.values());
            if(merchantIds.size() > updateMerchantIds.size()){
                List<Long> deletedIds = new ArrayList<Long>();
                for(Long id : merchantIds){
                    if(!updateMerchantIds.contains(id)){
                        deletedIds.add(id);
                    }
                }
                if(deletedIds.size() > 0){
                    batchDeleteIndex(deletedIds,indexName,type);
                }
            }
            if(updateMerchantList!=null && updateMerchantList.size()>0){
                logger.info("update merchantIds :" + updateMerchantIds);
                List<DataRecord> dataList = new ArrayList<DataRecord>();
                for(Merchant merchant : updateMerchantList){
                    dataList.add(new DataRecord<Merchant>(merchant));
                }
                if(dataList.size() > 0){
                    batchIncIndex(dataList,processors,indexName,type,companyId);
                }
            }
        }
        return GsonUtil.getGson().toJson(updateMerchantList);
    }

    private void batchIncIndex(List<DataRecord> dataList, List<Processor> processors,String indexName, String type,int companyId) throws Exception{
        ProcessorContext processorContext = new ProcessorContext(dataList);
        processorContext.setIndexName(indexName);
        processorContext.setIndexType(type);
        processorContext.setCompanyId(companyId);
        for(Processor processor : processors){
            processor.process(processorContext);
        }
        dataList.clear();
    }

    private void batchDeleteIndex(List<Long> deletedIds, String indexName, String indexType) throws ElasticsearchException, UnknownHostException {
        Client client = ESClient.getClient();
        IndexUpdater.delete(client,deletedIds,indexName,indexType);
        logger.info("deleted merchantIds : " + deletedIds +" indexName : "+ indexName +" indexType : "+ indexType);

        List<IncIndexProcessor.IndexInfo> indexIfoList = GeoIncIndexProcessorBuilder.getIndexInfoList();
        if(CollectionUtils.isNotEmpty(indexIfoList)){
            for(IncIndexProcessor.IndexInfo indexInfo : indexIfoList){
                IndexUpdater.delete(client,deletedIds,indexInfo.getIndexName(),indexInfo.getIndexType());
                logger.info("deleted merchantIds : " + deletedIds +" indexName : "+ indexInfo.getIndexName() +" indexType : "+ indexInfo.getIndexType());
            }
        }
    }

    public MerchantService getMerchantService() {
        return merchantService;
    }

    public void setMerchantService(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

}
