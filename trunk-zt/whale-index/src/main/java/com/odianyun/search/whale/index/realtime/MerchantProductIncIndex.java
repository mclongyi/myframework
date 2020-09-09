package com.odianyun.search.whale.index.realtime;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.common.util.GsonUtil;
import com.odianyun.search.whale.data.dao.CategoryDao;
import com.odianyun.search.whale.data.dao.MerchantProductDao;
import com.odianyun.search.whale.data.dao.ProductDao;
import com.odianyun.search.whale.data.model.MerchantProduct;
import com.odianyun.search.whale.data.saas.model.CompanyAppType;
import com.odianyun.search.whale.data.saas.model.ESClusterConfig;
import com.odianyun.search.whale.data.saas.service.CompanyRoutingService;
import com.odianyun.search.whale.data.service.MerchantProductService;
import com.odianyun.search.whale.es.api.ESClient;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.index.business.process.IncIndexProcessor.IndexInfo;
import com.odianyun.search.whale.index.business.process.build.IncIndexProcessorBuilder;
import com.odianyun.search.whale.processor.DataRecord;
import com.odianyun.search.whale.processor.Processor;
import com.odianyun.search.whale.processor.ProcessorContext;

public class MerchantProductIncIndex{
	
	static Logger logger = Logger.getLogger(MerchantProductIncIndex.class);

	static String _clientName = "whale-index";
	
	@Autowired
	MerchantProductService merchantProductService;
	
	@Autowired
	MerchantProductDao merchantProductDao;
	
	@Autowired
	ProductDao productDao;


	public String process(List<Long> merchantProductIds,boolean isSendIndexRequest,String indexName, 
			String type,int companyId) throws Exception{
		List<MerchantProduct> merchantProducts = null;
		if(merchantProductIds == null || merchantProductIds.size() == 0){
			return "merchantProductIds can not be null ";
		}
		if(isSendIndexRequest){
			List<Processor> processors = new IncIndexProcessorBuilder().build();
			Map<Long,MerchantProduct> merchantProductMap=merchantProductService.
					getMerchantProducts(merchantProductIds,companyId);
			merchantProducts = new ArrayList<MerchantProduct>(merchantProductMap.values()) ;
			List<Long> updateMerchantProductIds = new ArrayList<Long>(merchantProductMap.keySet());
			if(merchantProductIds.size() > merchantProductMap.size()){
				List<Long> deletedIds = new ArrayList<Long>();
				for(Long pmid : merchantProductIds){
					if(!updateMerchantProductIds.contains(pmid)){
						deletedIds.add(pmid);
						//增量索引优化-删除索引 每100个商品批量删除一次，20180913
						if(deletedIds.size() == 100) {
							batchDeleteIndex(deletedIds,indexName,type);
							deletedIds = new ArrayList<Long>();
						}
					}
				}
				if(deletedIds.size() > 0){
					batchDeleteIndex(deletedIds,indexName,type);					
				}
			}
			if(merchantProducts != null && merchantProducts.size() > 0){
				logger.info("updateIds : " + updateMerchantProductIds);
				List<DataRecord> dataList = new ArrayList<DataRecord>();
				for(MerchantProduct merchantProduct : merchantProducts){
					dataList.add(new DataRecord<MerchantProduct>(merchantProduct));
					//增量索引优化-新增索引每100个商品批量新增一次，20180913
					if(dataList.size() == 100){
						batchIncIndex(dataList,processors,indexName,type,companyId);
						dataList = new ArrayList<DataRecord>();
					}

				}
				if(dataList.size() > 0){
					batchIncIndex(dataList,processors,indexName,type,companyId);
				}
			}
			
		}
		return GsonUtil.getGson().toJson(merchantProducts);
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
	
	private void batchDeleteIndex(List<Long> deletedIds, String indexName, String indexType) throws ElasticsearchException, UnknownHostException{
		Client client = ESClient.getClient();
		IndexUpdater.delete(client,deletedIds,indexName,indexType);
		logger.info("deletedIds : " + deletedIds +" indexName : "+ indexName +" indexType : "+ indexType);

		List<IndexInfo> indexIfoList = IncIndexProcessorBuilder.getIndexInfoList();
		if(CollectionUtils.isNotEmpty(indexIfoList)){
			for(IndexInfo indexInfo : indexIfoList){
				IndexUpdater.delete(client,deletedIds,indexInfo.getIndexName(),indexInfo.getIndexType());
				logger.info("deletedIds : " + deletedIds +" indexName : "+ indexInfo.getIndexName() +" indexType : "+ indexInfo.getIndexType());
			}
		}
	}
	
}
