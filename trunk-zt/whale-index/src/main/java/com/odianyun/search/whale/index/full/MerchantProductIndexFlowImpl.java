package com.odianyun.search.whale.index.full;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.odianyun.search.whale.index.api.common.UpdateMessage;
import com.odianyun.search.whale.index.sort.MerchantProductSaleSorter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.data.manager.CompanyDBCacheManager;
import com.odianyun.search.whale.data.manager.SegmentManager;
import com.odianyun.search.whale.data.model.MerchantProduct;
import com.odianyun.search.whale.data.service.ConfigService;
import com.odianyun.search.whale.data.service.MerchantProductService;
import com.odianyun.search.whale.es.api.ESClient;
import com.odianyun.search.whale.es.api.ESService;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.index.api.common.UpdateType;
import com.odianyun.search.whale.index.business.process.build.FullIndexProcessorBuilder;
import com.odianyun.search.whale.index.business.process.build.IncIndexProcessorBuilder;
import com.odianyun.search.whale.index.convert.IDConverterManager;
import com.odianyun.search.whale.processor.DataRecord;
import com.odianyun.search.whale.processor.IndexFlow;
import com.odianyun.search.whale.processor.ProcessScheduler;
import com.odianyun.search.whale.processor.Processor;
import com.odianyun.search.whale.processor.ProcessorsBuilder;


public class MerchantProductIndexFlowImpl implements IndexFlow{
	
	SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
	
	static Logger logger = Logger.getLogger(MerchantProductIndexFlowImpl.class);

	String index_start_time;
	
	ProcessScheduler processScheduler;
	
	static int INDEX_NUM = 3;
	
	Set<Long> merchantSeriesIdSet;

	Set<Long> seriesParentIdSet;

	@Autowired
	MerchantProductIndexSwitcher esIndexSwitcher;
	
	@Autowired
	MerchantProductService merchantProductService;
	
	@Autowired
	ConfigService configService;

	List<Integer> companyIds;

	private String indexName;

	@Override
	public void init() throws Exception {
		reLoadCache();
		index_start_time=simpleDateFormat.format(new Date());
		initProcessScheduler();
		if(StringUtils.isBlank(indexName)){
			esIndexSwitcher.createIndex(index_start_time, IndexConstants.indexName_pre, "/es/"+IndexConstants.index_mapping_name);
		}

		IncIndexProcessorBuilder.registe(processScheduler.getIndexName(), processScheduler.getIndexType());
		MerchantProductSaleSorter.instance.init();
	}

	private void reLoadCache() throws Exception {
		merchantSeriesIdSet = new HashSet<Long>();
		seriesParentIdSet = new HashSet<Long>();
		this.companyIds = merchantProductService.queryCompanyIds();
		SegmentManager.getInstance().reload();
		CompanyDBCacheManager.instance.setCompanyIds(companyIds);
		CompanyDBCacheManager.instance.reloadAll();
	}

	@Override
	public boolean process() throws Exception {
		int pageSize = IndexConstants.pageSize;
		List<MerchantProduct> merchantProducts;
		if(CollectionUtils.isNotEmpty(companyIds)){
			for(Integer companyId : companyIds){
				processScheduler.setCompanyId(companyId);
				boolean hasNext = true;
				long maxId=-1l;
				while(hasNext){
					merchantProducts = merchantProductService.getMerchantProductsWithPage(maxId,pageSize,companyId);
					if(merchantProducts == null || merchantProducts.size() == 0 || merchantProducts.size() < pageSize){
						hasNext = false;
					}
					if(merchantProducts != null && merchantProducts.size()>0){
						maxId=merchantProducts.get(merchantProducts.size()-1).getId();
						for(MerchantProduct merchantProduct : merchantProducts){
							Long merchantSeriesId = merchantProduct.getMerchantSeriesId();
							if(null == merchantSeriesId || merchantSeriesId == 0){
								processScheduler.put(new DataRecord<MerchantProduct>(merchantProduct));
							}else{
								// 如果是系列品  则将该系列所有商品都查询至同一批次 进行计算
								List<DataRecord> dataRecordList = convertSeriesMpDataRecordList(merchantSeriesId,companyId);
								if(CollectionUtils.isNotEmpty(dataRecordList)){
									processScheduler.put(dataRecordList);
								}
							}
						}
						merchantProducts.clear();
					}
					Thread.sleep(1000);
				}
			}
		}

		processScheduler.close();
		return true;
	}

	private List<DataRecord> convertSeriesMpDataRecordList(Long merchantSeriesId, Integer companyId) throws Exception {
		List<DataRecord> dataRecorList = new ArrayList<DataRecord>();

		if(merchantSeriesIdSet.contains(merchantSeriesId)){
//			logger.info("merchantSeriesId : " + merchantSeriesId +" has calced !!! pass ");
			return dataRecorList;
		}
		List<Long> merchantSeriesIdList = new ArrayList<Long>();
		merchantSeriesIdList.add(merchantSeriesId);
		List<Long> mpIdList = IDConverterManager.instanse.convert(merchantSeriesIdList, UpdateType.merchant_series_id, companyId);
		List<MerchantProduct> merchantProductList = merchantProductService.getMerchantProductList(mpIdList, companyId);
		if(CollectionUtils.isNotEmpty(merchantProductList)){
			for(MerchantProduct mp : merchantProductList){
				dataRecorList.add(new DataRecord<MerchantProduct>(mp));
			}
		}
		merchantSeriesIdSet.add(merchantSeriesId);
//		logger.info("merchantSeriesId : " + merchantSeriesId +" calc firstTime mpidList : " + mpIdList);
		return dataRecorList;
	}

	private List<DataRecord> convertSeriesMpDataRecordList2(Long seriesParentId, Integer companyId) throws Exception {
		List<DataRecord> dataRecorList = new ArrayList<DataRecord>();

		if(seriesParentIdSet.contains(seriesParentId)){
//			logger.info("merchantSeriesId : " + merchantSeriesId +" has calced !!! pass ");
			return dataRecorList;
		}
		List<Long> seriesParentIdList = new ArrayList<Long>();
		seriesParentIdList.add(seriesParentId);
		List<Long> mpIdList = IDConverterManager.instanse.convert(seriesParentIdList, UpdateType.product_series_id, companyId);
		List<MerchantProduct> merchantProductList = merchantProductService.getMerchantProductList(mpIdList, companyId);
		if(CollectionUtils.isNotEmpty(merchantProductList)){
			for(MerchantProduct mp : merchantProductList){
				dataRecorList.add(new DataRecord<MerchantProduct>(mp));
			}
		}
		seriesParentIdSet.add(seriesParentId);
//		logger.info("merchantSeriesId : " + merchantSeriesId +" calc firstTime mpidList : " + mpIdList);
		return dataRecorList;
	}

	private void initProcessScheduler() throws Exception{
		if(processScheduler == null){
			ProcessorsBuilder fullIndexProcessorBuilder = new FullIndexProcessorBuilder();
			List<Processor> processors = fullIndexProcessorBuilder.build();
			processScheduler = new ProcessScheduler(processors,200);
		}
		if(StringUtils.isNotBlank(indexName)){
			processScheduler.setIndexName(indexName);
		}else{
			processScheduler.setIndexName(IndexConstants.indexName_pre + index_start_time);
		}
		processScheduler.setIndexType(IndexConstants.index_type);
	}

	@Override
	public void done(boolean needValidation) throws Exception {
		if (needValidation) {
			if (!esIndexSwitcher.validate(
					ESClient.getClient(),
					IndexConstants.indexName_pre.replace("_", ""), IndexConstants.index_type,
					index_start_time)) {
				ESService.deleteIndex(IndexConstants.indexName_pre + index_start_time);
				return;
			}
		}
		INDEX_NUM = configService.getInt("b2c_index_num", 3, IndexConstants.DEFAULT_COMPANY_ID);
		esIndexSwitcher.switchIndex(index_start_time,IndexConstants.indexName_pre,
						"/es/"+IndexConstants.index_mapping_name,IndexConstants.index_alias,needValidation,companyIds,INDEX_NUM);
		MerchantProductSaleSorter.instance.done();
		UpdateMessage message = new UpdateMessage();
		message.setUpdateType(UpdateType.FULL_INDEX);
		message.setVersion(processScheduler.getIndexName());
		IndexUpdateSender.sendMeaasge(message);
	}

	@Override
	public void afterDone(){
		IncIndexProcessorBuilder.remove(processScheduler.getIndexName(), processScheduler.getIndexType());

	}


	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
}
