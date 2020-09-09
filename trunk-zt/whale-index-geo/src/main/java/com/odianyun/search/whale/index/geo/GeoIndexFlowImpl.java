package com.odianyun.search.whale.index.geo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.odianyun.search.whale.index.geo.build.GeoFullIndexProcessorBuilder;
import com.odianyun.search.whale.index.geo.build.GeoIncIndexProcessorBuilder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.data.manager.CompanyDBCacheManager;
import com.odianyun.search.whale.data.manager.SegmentManager;
import com.odianyun.search.whale.data.model.Merchant;
import com.odianyun.search.whale.data.saas.model.CompanyAppType;
import com.odianyun.search.whale.data.saas.model.ESClusterConfig;
import com.odianyun.search.whale.data.saas.service.CompanyRoutingService;
import com.odianyun.search.whale.data.service.MerchantProductService;
import com.odianyun.search.whale.data.service.MerchantService;
import com.odianyun.search.whale.es.api.ESClient;
import com.odianyun.search.whale.es.api.ESService;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.index.api.common.MerchantAreaIndexContants;
import com.odianyun.search.whale.processor.DataRecord;
import com.odianyun.search.whale.processor.IndexFlow;
import com.odianyun.search.whale.processor.ProcessScheduler;
import com.odianyun.search.whale.processor.Processor;


public class GeoIndexFlowImpl implements IndexFlow {

	SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
	
	String index_start_time;
	
	ProcessScheduler processScheduler;
	
	
	GeoIndexSwitcher geoSwitcher;
	
	
	MerchantService merchantService;
	
	@Autowired
	MerchantProductService merchantProductService;

	List<Integer> companyIds;

	static int INDEX_NUM = 3;

	private String indexName;

	@Override
	public void init() throws Exception {
		reloadCache();
		index_start_time=simpleDateFormat.format(new Date());
		if(processScheduler==null){
			List<Processor> processors = new GeoFullIndexProcessorBuilder().build();
			processScheduler = new ProcessScheduler(processors,200);
//			processScheduler.schedule();
		}

		processScheduler.setIndexType(MerchantAreaIndexContants.index_type);
		if(StringUtils.isNotBlank(indexName)){
			processScheduler.setIndexName(indexName);
		}else{
			processScheduler.setIndexName(MerchantAreaIndexContants.indexName_pre + index_start_time);
			geoSwitcher.createIndex(index_start_time, MerchantAreaIndexContants.indexName_pre, "/es/"+MerchantAreaIndexContants.index_mapping_name);

		}


		GeoIncIndexProcessorBuilder.registe(processScheduler.getIndexName(),processScheduler.getIndexType());
	}

	public void reloadCache() throws Exception {
		this.companyIds = merchantProductService.queryCompanyIds();
		CompanyDBCacheManager.instance.setCompanyIds(companyIds);
		if(CollectionUtils.isNotEmpty(companyIds)){
			for(Integer id : companyIds){
				CompanyDBCacheManager.instance.reload("com.odianyun.search.whale.data.service.impl.MerchantServiceImpl", id);
				CompanyDBCacheManager.instance.reload("com.odianyun.search.whale.data.geo.service.impl.POIServiceImpl", id);
			}
		}
	}

	@Override
	public boolean process() throws Exception {
		int pageSize = IndexConstants.pageSize;
		List<Merchant> merchantList;
		if(CollectionUtils.isNotEmpty(companyIds)){
			for(Integer companyId : companyIds){
				int pageNo = 1;
				boolean hasNext = true;
				processScheduler.setCompanyId(companyId);
				while(hasNext){
					merchantList = merchantService.getMerchantsWithPage(pageNo, pageSize,companyId);
					if(CollectionUtils.isEmpty(merchantList)){
						hasNext = false;
					}else{
						for(Merchant merchant:merchantList){
							processScheduler.put(new DataRecord<Merchant>(merchant));
						}
						merchantList.clear();
					}
					pageNo++;
				}
			}
		}
		
		processScheduler.close();
		return true;
	}

	@Override
	public void done(boolean needValidation) throws Exception {
		if (needValidation) {
			if (!geoSwitcher.validate(
					ESClient.getClient(),
					MerchantAreaIndexContants.indexName_pre.replace("_", ""), MerchantAreaIndexContants.index_type,
					index_start_time)) {
				ESService.deleteIndex( MerchantAreaIndexContants.indexName_pre + index_start_time);
				return;
			}
		}
		geoSwitcher.switchIndex(index_start_time, MerchantAreaIndexContants.indexName_pre,
				"/es/" + MerchantAreaIndexContants.index_mapping_name, MerchantAreaIndexContants.index_alias, needValidation, companyIds,INDEX_NUM);
	}

	@Override
	public void afterDone() {
		GeoIncIndexProcessorBuilder.remove(processScheduler.getIndexName(), processScheduler.getIndexType());
	}

	public void setGeoSwitcher(GeoIndexSwitcher geoSwitcher) {
		this.geoSwitcher = geoSwitcher;
	}

	public void setMerchantService(MerchantService merchantService) {
		this.merchantService = merchantService;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
}
