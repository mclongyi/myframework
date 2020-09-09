package com.odianyun.search.whale.index.geo;


import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

import com.odianyun.search.whale.data.model.geo.BusinessTime;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.indices.IndexMissingException;

import com.odianyun.search.whale.common.util.GsonUtil;
import com.odianyun.search.whale.data.geo.service.O2OStoreService;
import com.odianyun.search.whale.data.model.geo.O2OStore;
import com.odianyun.search.whale.es.api.ESService;
import com.odianyun.search.whale.es.api.EsIndexSwitcher;
import com.odianyun.search.whale.es.request.ESIndexRequest;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.index.api.common.MerchantAreaIndexContants;
import com.odianyun.search.whale.index.geo.model.GeoStore;
import com.odianyun.search.whale.index.geo.model.Shape;


public class GeoIndexSwitcher extends EsIndexSwitcher{
	static Logger logger = Logger.getLogger(GeoIndexSwitcher.class);
	public static final String Splitter_Point = "\\}\\{";

//	O2OStoreService storeService;
	
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
		logger.info(clusterName+ " index " + fullIndexName + " index_type " + index_type+" has doc num : " + newIndexDocCount);

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
		logger.info(clusterName+ " index " + indexNameAlias + " index_type " + index_type+" has doc num : " + nowIndexDocCount);

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
		/*int pageNo = 1;
		int pageSize = IndexConstants.pageSize;
		boolean hasNext = true;
		List<O2OStore> storelist;
		List<ESIndexRequest> esIndexRequests=new ArrayList<ESIndexRequest>();
		
		while(hasNext){
			storelist=storeService.queryO2OStoresWithPage(pageNo,pageSize);
			if(storelist == null || storelist.size() == 0 || storelist.size() < pageSize){
				hasNext = false;
			}
			if(storelist!=null){
				for(O2OStore store:storelist){
//					Map<String,Object> map=convert(store);
					String jsonData = convert(store);
					ESIndexRequest esIndexRequest=new ESIndexRequest(indexName, 
							MerchantAreaIndexContants.index_type, store.getMerchantId().toString(), jsonData);
					esIndexRequests.add(esIndexRequest);
					if(esIndexRequests.size()>=100){
						org.elasticsearch.action.bulk.BulkResponse bulkResponse = ESService.indexJsonData(esIndexRequests);
						if(bulkResponse != null && bulkResponse.hasFailures()){
							logger.error("index error : "+bulkResponse.buildFailureMessage());
							throw new Exception("index error : "+bulkResponse.buildFailureMessage());
						}
						esIndexRequests.clear();
					}
				}
				if(esIndexRequests.size()>0){
					org.elasticsearch.action.bulk.BulkResponse bulkResponse = ESService.indexJsonData(esIndexRequests);
					if(bulkResponse != null && bulkResponse.hasFailures()){
						logger.error("index error : "+bulkResponse.buildFailureMessage());
						throw new Exception("index error : "+bulkResponse.buildFailureMessage());
					}
					esIndexRequests.clear();
				}
				storelist.clear();
			}
			pageNo++;
		}*/
	}
	

	
	public static String convert(O2OStore store) throws Exception{
		if(store==null)
			return null;
		GeoStore geos = new GeoStore();
		Long merchantId = store.getMerchantId();
		if(merchantId!=null)
			geos.setMerchantId(merchantId);
		Long shopId = store.getShopId();
		if(shopId!=null)
			geos.setShopId(shopId);
		Long companyId = store.getCompany_id();
		if(companyId!=null)
			geos.setCompanyId(companyId);
		String codeSearch = store.getCodeSearch();
		if(codeSearch!=null)
			geos.setCodeSearch(codeSearch);
		String tag_words = store.getTag_words();
		if(tag_words!=null)
			geos.setTag_words(tag_words);
		Integer merchantFlag = store.getMerchantFlag();
		if(merchantFlag!=null){
			geos.setMerchantFlag(merchantFlag);
		}
		String location = store.getLocation();
		if(location!=null)
			geos.setLocation(location);
		String polygon = store.getPolygon();
		if(polygon!=null)
			geos.setPolygon(new Shape("multipolygon",polygon));
		if(merchantFlag == null || (1==merchantFlag && location == null )){
			geos.setMerchantFlag(-1);
		}
		Integer businessState = store.getBusinessState();
		if(businessState != null){
			geos.setBusiness_state(businessState);
		}
		List<BusinessTime> businessTimes = store.getBusinessTimes();
		if(CollectionUtils.isNotEmpty(businessTimes)){
			geos.setBusiness_times(businessTimes);
		}
		Integer hasInSiteService = store.getHasInSiteService();
		if(hasInSiteService != null){
			geos.setHasInSiteService(hasInSiteService);
		}
		Long parentId=store.getParentId();
		if(parentId!=null){
			geos.setParentId(parentId);
		}
		String areaCodes = store.getAreaCodes();
		if (areaCodes != null) {
			geos.setAreaCodes(areaCodes);
		}
		
		//外卖增加merchantType
		Integer merchantType = store.getMerchantType();
		if (merchantType != null) {
			geos.setMerchantType(merchantType);
		}
		
		String ret=GsonUtil.getGson().toJson(geos);
		if(ret!=null)
			ret = ret.replace("\"[", "[").replace("]\"", "]");
		return ret;
	}
	
	
//	public void setStoreService(O2OStoreService storeService) {
//		this.storeService = storeService;
//	}

	@Override
	public void incIndexRemediation(String start_index_time, String indexName,List<Integer> companyIds) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
    
}
