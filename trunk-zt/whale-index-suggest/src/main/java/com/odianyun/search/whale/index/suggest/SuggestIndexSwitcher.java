package com.odianyun.search.whale.index.suggest;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.indices.IndexMissingException;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.data.model.suggest.SuggestWord;
import com.odianyun.search.whale.data.suggest.service.SuggestWordService;
import com.odianyun.search.whale.es.api.ESService;
import com.odianyun.search.whale.es.api.EsIndexSwitcher;
import com.odianyun.search.whale.es.request.ESIndexRequest;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.index.api.common.SuggestIndexConstants;
import com.odianyun.search.whale.index.api.common.SuggestIndexFieldConstants;


public class SuggestIndexSwitcher extends EsIndexSwitcher{
	static Logger logger = Logger.getLogger(SuggestIndexSwitcher.class);

	@Autowired
	SuggestWordService suggestWordService;
	
	@Override
	public boolean validate(String index_name) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean validate(Client client, String index_name, String index_type, String index_version) throws Exception{
		String fullIndexName = index_name + "_" + index_version;
        String indexNameAlias = index_name + "_alias" ;
		boolean exists = ESService.indexExists(client, fullIndexName);
		
		String clusterName = "";
		if(client.settings() != null){
			clusterName = client.settings().get("cluster.name");
		}
		if(!exists){
			logger.info(clusterName + " index " + fullIndexName +" don't exist !!!");
			return exists;
		}
		
		Thread.sleep(1000*10);
		ESSearchRequest esSearchRequest = new ESSearchRequest(fullIndexName, index_type);
		esSearchRequest.setSearchType(SearchType.COUNT);
		esSearchRequest.setQueryBuilder(QueryBuilders.boolQuery());
		CountResponse resp = ESService.count(client, esSearchRequest);
		long newIndexDocCount = resp.getCount();
		logger.info(clusterName + " index " + fullIndexName +" has doc num : " + newIndexDocCount);

		esSearchRequest.setIndexName(indexNameAlias);
        try {
            resp = ESService.count(client, esSearchRequest);
        } catch (IndexMissingException e) {
            logger.warn(e.getMessage());
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        long nowIndexDocCount = resp.getCount();
		logger.info(clusterName + " index " + indexNameAlias +" has doc num : " + nowIndexDocCount);

		exists = validation(nowIndexDocCount,newIndexDocCount);
		logger.info(clusterName + " index " + fullIndexName +" validation: " + exists);
		
		return exists;
	}
	
	private Boolean validation(long now_count,long new_count) throws Exception{
		if(now_count==0){
			return true;
		}
		//double dbRatio = configService.getDouble("dbRatio", 0.8);
		double dbRatio =0.8;
		BigDecimal new_count_BigDecimal=new BigDecimal(new_count);
		BigDecimal now_count_BigDecimal=new BigDecimal(now_count);
		double ratio=new_count_BigDecimal.divide(now_count_BigDecimal,new MathContext(2)).doubleValue();
		if(ratio<dbRatio){
			return false;
		}
		return true;
	}

	@Override
	public void indexAllDataWithPage(String indexName) throws Exception {
		int pageNo = 1;
		int pageSize = IndexConstants.pageSize;
		boolean hasNext = true;
		List<SuggestWord> wordlist;
		List<ESIndexRequest> esIndexRequests=new ArrayList<ESIndexRequest>();
		
		while(hasNext){
			wordlist=suggestWordService.getSuggestWordsWithPage(pageNo,pageSize);
			if(wordlist == null || wordlist.size() == 0 || wordlist.size() < pageSize){
				hasNext = false;
			}
			if(wordlist!=null){
				for(SuggestWord word : wordlist){
					Map<String,Object> map=convert(word);
					ESIndexRequest esIndexRequest=new ESIndexRequest(indexName, 
							SuggestIndexConstants.index_type, word.getKeyword(), map);
					esIndexRequests.add(esIndexRequest);
					if(esIndexRequests.size()>=100){
						org.elasticsearch.action.bulk.BulkResponse bulkResponse = ESService.index(esIndexRequests);
						if(bulkResponse != null && bulkResponse.hasFailures()){
							logger.error("index error : "+bulkResponse.buildFailureMessage());
							throw new Exception("index error : "+bulkResponse.buildFailureMessage());
						}
						esIndexRequests.clear();
					}
				}
				if(esIndexRequests.size()>0){
					org.elasticsearch.action.bulk.BulkResponse bulkResponse = ESService.index(esIndexRequests);
					if(bulkResponse != null && bulkResponse.hasFailures()){
						logger.error("index error : "+bulkResponse.buildFailureMessage());
						throw new Exception("index error : "+bulkResponse.buildFailureMessage());
					}
					esIndexRequests.clear();
				}
				wordlist.clear();
			}
			pageNo++;
		}
	}

	public static Map<String,Object> convert(Object obj) throws Exception{
		Map<String,Object> map=new HashMap<String,Object>();
		SuggestWord word = (SuggestWord)obj;
		map.put(SuggestIndexFieldConstants.KEYWORD, word.getKeyword());
		Map<String,Object> subMap=new HashMap<String,Object>();
		Set<String> inputSet = new HashSet<>();
		String[] inputs = word.getInput().split(",");
		for(String str : inputs){
			inputSet.add(str);
		}
		Map<String,Object> contextMap = new HashMap<String,Object>();
		contextMap.put(SuggestIndexFieldConstants.COMPANYID,word.getCompanyId());
		contextMap.put(SuggestIndexFieldConstants.MERCHANTID,word.getMerchantIdList());
		contextMap.put(SuggestIndexFieldConstants.ISAREA,word.getIsArea());

		subMap.put(SuggestIndexFieldConstants.INPUT, new ArrayList<>(inputSet));
		subMap.put(SuggestIndexFieldConstants.OUTPUT, word.getKeyword());
		subMap.put(SuggestIndexFieldConstants.WEIGHT, word.getSearchFrequency());
		subMap.put(SuggestIndexFieldConstants.CONTEXT,contextMap);
		map.put(SuggestIndexFieldConstants.SUGGEST, subMap);
		return map;
	}

	@Override
	public void incIndexRemediation(String start_index_time, String indexName,List<Integer> companyIds) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
