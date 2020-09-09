package com.odianyun.search.whale.index.suggest.common;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.client.Client;

import com.odianyun.search.whale.data.model.suggest.SuggestWord;
import com.odianyun.search.whale.es.api.ESService;
import com.odianyun.search.whale.es.request.ESDeleteRequest;
import com.odianyun.search.whale.es.request.ESIndexRequest;
import com.odianyun.search.whale.index.suggest.SuggestIndexSwitcher;

public class IndexUpdater {
	
	static Logger logger = Logger.getLogger(IndexUpdater.class);

	public static void update(Client client,List<SuggestWord> suggestWords, String indexName, String type) throws Exception {
    	List<ESIndexRequest> esIndexRequests = new ArrayList<ESIndexRequest>();
//    	String indexName = IndexConstants.index_alias;
//    	String type = IndexConstants.index_type;
    	for(SuggestWord suggestWord : suggestWords){
    		Map<String,Object> map = SuggestIndexSwitcher.convert(suggestWord);
			String suggestIndexId = suggestWord.getKeyword().toString() + suggestWord.getCompanyId();
    		ESIndexRequest esIndexRequest = new ESIndexRequest(indexName,type,suggestIndexId,map);
    		esIndexRequests.add(esIndexRequest);
    	}
//    	ESService.index(esIndexRequests);
    	org.elasticsearch.action.bulk.BulkResponse bulkResponse = ESService.index(client,esIndexRequests);
		if(bulkResponse != null && bulkResponse.hasFailures()){
			logger.error("index error : "+bulkResponse.buildFailureMessage());
		}else{
			String clusterName = "";
			if(client.settings() != null){
				clusterName = client.settings().get("cluster.name");
			}
			logger.info("send to "+ clusterName +" index "+ indexName + " type "+ type + " doc size: " + suggestWords.size());
		}
		
    }
    
    public static void delete(Client client,List<Long> ids, String indexName, String type) throws ElasticsearchException, UnknownHostException{
    	List<String> ids_str=new ArrayList<String>();
    	for(Long id:ids){
    		ids_str.add(String.valueOf(id));
    	}
//    	String indexName = IndexConstants.index_alias;
//    	String type = IndexConstants.index_type;
    	ESDeleteRequest esDeleteRequest = new ESDeleteRequest(indexName, type, ids_str);
//    	ESService.delete(esDeleteRequest);
    	org.elasticsearch.action.bulk.BulkResponse bulkResponse = ESService.delete(client,esDeleteRequest);
		if(bulkResponse != null && bulkResponse.hasFailures()){
			logger.error("delete error : "+bulkResponse.buildFailureMessage());
		}
    }

}
