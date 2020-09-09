package com.odianyun.search.whale.index.points.full;


import com.odianyun.search.whale.data.manager.CompanyDBCacheManager;
import com.odianyun.search.whale.data.manager.SegmentManager;
import com.odianyun.search.whale.data.model.PointsMallProduct;
import com.odianyun.search.whale.data.service.ConfigService;
import com.odianyun.search.whale.data.service.MerchantProductService;
import com.odianyun.search.whale.data.service.PointsMallProductService;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.index.business.process.build.FullIndexProcessorBuilder;
import com.odianyun.search.whale.index.business.process.build.IncIndexProcessorBuilder;
import com.odianyun.search.whale.index.full.MerchantProductIndexSwitcher;
import com.odianyun.search.whale.index.sort.MerchantProductSaleSorter;
import com.odianyun.search.whale.processor.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;


public class PointsMallProductIndexFlowImpl implements IndexFlow{
	
	SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
	
	static Logger logger = Logger.getLogger(PointsMallProductIndexFlowImpl.class);

	String index_start_time;
	
	ProcessScheduler processScheduler;

	@Autowired
	MerchantProductService merchantProductService;

	@Autowired
	PointsMallProductService pointsMallProductService;

	@Autowired
	ConfigService configService;

	List<Integer> companyIds;

	private String indexName;

	@Override
	public void init() throws Exception {
		reLoadCache();
		index_start_time=simpleDateFormat.format(new Date());
		initProcessScheduler();

		IncIndexProcessorBuilder.registe(processScheduler.getIndexName(), processScheduler.getIndexType());
		MerchantProductSaleSorter.instance.init();
	}

	private void reLoadCache() throws Exception {

		this.companyIds = merchantProductService.queryCompanyIds();
		SegmentManager.getInstance().reload();
		CompanyDBCacheManager.instance.setCompanyIds(companyIds);
		CompanyDBCacheManager.instance.reloadAll();
	}

	@Override
	public boolean process() throws Exception {
		int pageSize = IndexConstants.pageSize;
		List<PointsMallProduct> merchantProducts;
		if(CollectionUtils.isNotEmpty(companyIds)){
			for(Integer companyId : companyIds){
				processScheduler.setCompanyId(companyId);
				boolean hasNext = true;
				long maxId=-1l;
				while(hasNext){
					merchantProducts = pointsMallProductService.getPointsMallProductsWithPage(maxId,pageSize,companyId);
					if(merchantProducts == null || merchantProducts.size() == 0 || merchantProducts.size() < pageSize){
						hasNext = false;
					}
					if(merchantProducts != null && merchantProducts.size()>0){
						maxId=merchantProducts.get(merchantProducts.size()-1).getId();
						for(PointsMallProduct merchantProduct : merchantProducts){
							processScheduler.put(new DataRecord<PointsMallProduct>(merchantProduct));

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

	private void initProcessScheduler() throws Exception{
		if(processScheduler == null){
			ProcessorsBuilder fullIndexProcessorBuilder = new FullIndexProcessorBuilder();
			List<Processor> processors = fullIndexProcessorBuilder.build();
			processScheduler = new ProcessScheduler(processors,200);
		}
		processScheduler.setIndexName(indexName);
		processScheduler.setIndexType(IndexConstants.index_type);
	}

	@Override
	public void done(boolean needValidation) throws Exception {

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
