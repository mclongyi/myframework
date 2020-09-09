package com.odianyun.search.whale.index.business.process.build;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.odianyun.search.whale.index.business.process.*;
import com.odianyun.search.whale.index.business.process.IncIndexProcessor.IndexInfo;
import com.odianyun.search.whale.processor.Processor;
import com.odianyun.search.whale.processor.ProcessorsBuilder;

public class IncIndexProcessorBuilder implements ProcessorsBuilder {

	static IncIndexProcessor incIndexProcessor = new IncIndexProcessor();
	
	List<Processor> processors = new ArrayList<Processor>();

	static Logger logger = Logger.getLogger(IncIndexProcessorBuilder.class);

	@Override
	public List<Processor> build() throws Exception {
		
		if(CollectionUtils.isNotEmpty(processors)){
			return processors;
		}
		synchronized (IncIndexProcessorBuilder.class) {
			if(CollectionUtils.isNotEmpty(processors)){
				return processors;
			}
			processors.add(new BusinessProductProcessor());
			processors.add(new ProductPicUrlProcessor());
			processors.add(new SearchWordProcessor());
			processors.add(new ProductPriceProcessor());
			processors.add(new ProductMerchantProcessor());
			processors.add(new ProductBrandProcessor());
			processors.add(new ProductCategoryProcessor());
			processors.add(new ProductAttributeProcessor());
			processors.add(new ProductStockProcessor());
			processors.add(new MerchantVolume4SaleProcessor());
			processors.add(new MerchantProductWeightProcessor());
			processors.add(new MerchantRateProcessor());
			processors.add(new ProductSeriesProcessor());
			processors.add(new DistributionProcessor());
			processors.add(new SegmentProcessor());
			processors.add(new ProductScriptProcessor());
			processors.add(new ProductSaleAreasProcessor());
			processors.add(new MerchantAreaCodeProcessor());
			processors.add(new MerchantProductRefIdProcess());
			processors.add(new PointsBusinessProductProcessor());
			processors.add(new CurrentPointsBusinessProductProcessor());

			processors.add(new MerchantProductSearchProcessor());
			processors.add(incIndexProcessor);
		}
		

		return processors;
	}
	
	public static void registe(String indexName,String indexType){
		IndexInfo indexInfo = new IndexInfo(indexName,indexType);
		incIndexProcessor.register(indexInfo);
		logger.info("IncIndexProcessorBuilder register ( indexName : "+indexName+" indexType : "+indexType + ")");
		logger.info("incIndexProcessor indexInfo size : " + incIndexProcessor.indexInfoSize());
	}
	
	public static void remove(String indexName,String indexType){
		IndexInfo indexInfo = new IndexInfo(indexName,indexType);
		incIndexProcessor.remove(indexInfo);
		logger.info("IncIndexProcessorBuilder remove ( indexName : "+indexName+" indexType : "+indexType + ")");
		logger.info("incIndexProcessor indexInfo size : " + incIndexProcessor.indexInfoSize());
	}
	
	public static List<IndexInfo> getIndexInfoList(){
		return incIndexProcessor.getIndexInfoList();
	}
	
	public static void clear(){
		incIndexProcessor.clear();
		logger.info("IncIndexProcessorBuilder clear");
		logger.info("incIndexProcessor indexInfo size : " + incIndexProcessor.indexInfoSize());
	}
	
	public static void main(String[] args) {
		List<IndexInfo> indexInfoList = new ArrayList<IndexInfo>();
		IndexInfo indexInfo = new IndexInfo("b2c","mp");
		indexInfoList.add(indexInfo);
		
		System.out.println(indexInfoList);

		IndexInfo indexInfo2 = new IndexInfo("b2c","mp");
		indexInfoList.remove(indexInfo2);
		System.out.println(indexInfoList);

	}
	
}
