package com.odianyun.search.whale.index.realtime;

import java.util.List;

import com.odianyun.search.whale.index.api.common.*;
import com.odianyun.search.whale.index.geo.realtime.GeoIncIndex;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.data.manager.UpdateConsumer;
import com.odianyun.search.whale.data.saas.model.CompanyAppType;
import com.odianyun.search.whale.data.saas.service.CompanyRoutingService;
import com.odianyun.search.whale.index.convert.IDConverterManager;

public class IndexUpdateConsumer extends UpdateConsumer{

	static Logger logger = Logger.getLogger(IndexUpdateConsumer.class);

	public static final String INDEX_CONSUMER_SUFFIX = "index";

	@Override
	public void startConsumerReload(String topic) {
		String consumerId = genConsumerIdWithSuffix(INDEX_CONSUMER_SUFFIX);
		startConsumerReload(topic,consumerId);
	}

	@Override
	protected void updateByIds(List<Long> ids, UpdateType updateType, int companyId) throws Exception {
		super.updateByIds(ids, updateType, companyId);
		updateIndex(ids, updateType,companyId);
	}

	@Autowired
	MerchantProductIncIndex merchantProductIncIndex;

	@Autowired
	GeoIncIndex geoIncIndex;
	
	public void updateIndex(List<Long> ids, UpdateType updateType,int companyId) throws Exception {

		if(CollectionUtils.isNotEmpty(ids)){
			String indexAlias = null;
			if(isStartWithOplusO){
				indexAlias = OplusOIndexConstants.index_alias;
			}
			// 更新geo索引
			if(UpdateType.GEO_MERCHANT_ID.equals(updateType)){
				if(StringUtils.isBlank(indexAlias)){
					indexAlias = MerchantAreaIndexContants.index_alias;
				}
				geoIncIndex.process(ids,true,indexAlias,MerchantAreaIndexContants.index_type,companyId);
			}else{
				//更新b2c索引
				List<Long> convertIds = IDConverterManager.instanse.convert(ids, updateType, companyId);
				if(CollectionUtils.isNotEmpty(convertIds)){
					if(StringUtils.isBlank(indexAlias)){
						indexAlias = IndexConstants.index_alias;
					}
					merchantProductIncIndex.process(convertIds, true,indexAlias,IndexConstants.index_type,companyId);
				}
			}


		}
		
	}
	
}
