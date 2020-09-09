package com.odianyun.search.whale.index.opluso.full;


import com.odianyun.search.whale.data.manager.CompanyDBCacheManager;
import com.odianyun.search.whale.data.manager.SegmentManager;
import com.odianyun.search.whale.data.model.MerchantProduct;
import com.odianyun.search.whale.data.service.ConfigService;
import com.odianyun.search.whale.data.service.MerchantProductService;
import com.odianyun.search.whale.es.api.ESClient;
import com.odianyun.search.whale.es.api.ESService;
import com.odianyun.search.whale.index.api.common.*;
import com.odianyun.search.whale.index.business.process.build.FullIndexProcessorBuilder;
import com.odianyun.search.whale.index.business.process.build.IncIndexProcessorBuilder;
import com.odianyun.search.whale.index.convert.IDConverterManager;
import com.odianyun.search.whale.index.full.IndexUpdateSender;
import com.odianyun.search.whale.index.full.MerchantProductIndexFlowImpl;
import com.odianyun.search.whale.index.full.MerchantProductIndexSwitcher;
import com.odianyun.search.whale.index.geo.GeoIndexFlowImpl;
import com.odianyun.search.whale.index.geo.GeoIndexSwitcher;
import com.odianyun.search.whale.index.geo.build.GeoIncIndexProcessorBuilder;
import com.odianyun.search.whale.index.sort.MerchantProductSaleSorter;
import com.odianyun.search.whale.processor.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;


public class OplusOIndexFlowImpl implements IndexFlow{
	
	SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
	
	static Logger logger = Logger.getLogger(OplusOIndexFlowImpl.class);

	String index_start_time;

	@Autowired
	MerchantProductIndexFlowImpl merchantProductIndexFlow;

	@Autowired
	GeoIndexFlowImpl geoIndexFlow;

	@Autowired
	OplusOIndexSwitcher oplusOIndexSwitcher;

	@Autowired
	GeoIndexSwitcher geoSwitcher;

	@Autowired
	MerchantProductIndexSwitcher esIndexSwitcher;


	static int INDEX_NUM = 3;

	@Autowired
	ConfigService configService;

	String indexName;

	@Override
	public void init() throws Exception {
		index_start_time=simpleDateFormat.format(new Date());
		indexName = OplusOIndexConstants.indexName_pre + index_start_time;
		merchantProductIndexFlow.setIndexName(indexName);
		merchantProductIndexFlow.init();
		geoIndexFlow.setIndexName(indexName);
		geoIndexFlow.init();
		oplusOIndexSwitcher.createIndex(index_start_time, OplusOIndexConstants.indexName_pre, "/es/"+OplusOIndexConstants.index_mapping_name);
		oplusOIndexSwitcher.createIndex(index_start_time, PointsMpIndexConstants.indexName_pre, "/es/"+PointsMpIndexConstants.index_mapping_name);

	}

	@Override
	public boolean process() throws Exception {
		geoIndexFlow.process();
		merchantProductIndexFlow.process();
		return true;
	}

	@Override
	public void done(boolean needValidation) throws Exception {
		if (needValidation) {
			String indexNamePre = OplusOIndexConstants.indexName_pre.replace("_", "");
			boolean validate = esIndexSwitcher.validate(ESClient.getClient(),indexNamePre, IndexConstants.index_type, index_start_time) &&
					geoSwitcher.validate(ESClient.getClient(),indexNamePre, MerchantAreaIndexContants.index_type, index_start_time);
			if (!validate) {
				ESService.deleteIndex(indexName);
				return;
			}
		}

		INDEX_NUM = configService.getInt("opluso_index_num", 3, IndexConstants.DEFAULT_COMPANY_ID);
		oplusOIndexSwitcher.switchIndex(index_start_time,OplusOIndexConstants.indexName_pre,
				"/es/"+OplusOIndexConstants.index_mapping_name,OplusOIndexConstants.index_alias,needValidation,null,INDEX_NUM);
		oplusOIndexSwitcher.switchIndex(index_start_time,PointsMpIndexConstants.indexName_pre,
				"/es/"+OplusOIndexConstants.index_mapping_name,PointsMpIndexConstants.index_alias,needValidation,null,INDEX_NUM);

		MerchantProductSaleSorter.instance.done();
		UpdateMessage message = new UpdateMessage();
		message.setUpdateType(UpdateType.FULL_INDEX);
		message.setVersion(indexName);
		IndexUpdateSender.sendMeaasge(message);
	}

	@Override
	public void afterDone(){
		merchantProductIndexFlow.afterDone();
		geoIndexFlow.afterDone();
	}


}
