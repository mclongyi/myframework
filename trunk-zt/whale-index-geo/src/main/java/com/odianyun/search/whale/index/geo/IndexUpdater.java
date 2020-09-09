package com.odianyun.search.whale.index.geo;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.client.Client;

import com.odianyun.search.whale.data.model.geo.O2OStore;
import com.odianyun.search.whale.data.model.suggest.SuggestWord;
import com.odianyun.search.whale.es.api.ESService;
import com.odianyun.search.whale.es.request.ESDeleteRequest;
import com.odianyun.search.whale.es.request.ESIndexRequest;
import com.odianyun.search.whale.index.api.common.MerchantAreaIndexContants;

public class IndexUpdater {

	static Logger logger = Logger.getLogger(IndexUpdater.class);

	public static void update(Client client,List<O2OStore> o2oStores, String indexName, String type) throws Exception {
		List<ESIndexRequest> esIndexRequests = new ArrayList<ESIndexRequest>();
		for(O2OStore o2oStore : o2oStores){
			String jsonData = GeoIndexSwitcher.convert(o2oStore);
			ESIndexRequest esIndexRequest=new ESIndexRequest(indexName,
					type, o2oStore.getMerchantId().toString(), jsonData);
			esIndexRequests.add(esIndexRequest);
		}
		org.elasticsearch.action.bulk.BulkResponse bulkResponse = ESService.indexJsonData(client,esIndexRequests);
		if(bulkResponse != null && bulkResponse.hasFailures()){
			logger.error("index error : "+bulkResponse.buildFailureMessage());
		}else{
			String clusterName = "";
			if(client.settings() != null){
				clusterName = client.settings().get("cluster.name");
			}
			logger.info("send to "+clusterName+" index "+ indexName + " type " + type +" doc size: " + o2oStores.size());
		}

	}

	public static void delete(Client client,List<Long> ids, String indexName, String type) throws ElasticsearchException, UnknownHostException{
		List<String> ids_str=new ArrayList<String>();
		for(Long id:ids){
			ids_str.add(String.valueOf(id));
		}
		ESDeleteRequest esDeleteRequest = new ESDeleteRequest(indexName, type, ids_str);
		org.elasticsearch.action.bulk.BulkResponse bulkResponse = ESService.delete(client,esDeleteRequest);
		if(bulkResponse != null && bulkResponse.hasFailures()){
			logger.error("delete error : "+bulkResponse.buildFailureMessage());
		}
	}

}
