package com.odianyun.search.whale.es.api;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.odianyun.search.whale.es.request.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.close.CloseIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.close.CloseIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.open.OpenIndexResponse;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequestBuilder;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.count.CountRequestBuilder;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.exists.ExistsRequestBuilder;
import org.elasticsearch.action.exists.ExistsResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.suggest.SuggestRequestBuilder;
import org.elasticsearch.action.suggest.SuggestResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.lang3.StringUtils;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.FilteredQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.elasticsearch.search.suggest.term.TermSuggestionBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.odianyun.search.whale.common.util.HttpClientUtil;
import com.odianyun.search.whale.es.constants.ESSettingConstants;

public class ESService {

	static Logger logger = Logger.getLogger(ESService.class);

	public static BulkResponse index(List<ESIndexRequest> esIndexRequests) throws ElasticsearchException, UnknownHostException{
		return index(ESClient.getClient(), esIndexRequests);
	}
	
	public static BulkResponse index(Client client, List<ESIndexRequest> esIndexRequests) throws ElasticsearchException, UnknownHostException{
		BulkRequestBuilder bulkRequest = client.prepareBulk();
        for(ESIndexRequest indexRequest:esIndexRequests){
			IndexRequestBuilder indexRequestBuilder = ESClient.getClient()
					.prepareIndex(indexRequest.getIndexName(), indexRequest.getType(), indexRequest.getId())
					.setSource(indexRequest.getData());
        	String parent = indexRequest.getParent();
			//如果设置了父子文档关系 需指定父文档doc id
			if(StringUtils.isNotBlank(parent)){
				indexRequestBuilder.setParent(parent);
			}
			bulkRequest.add(indexRequestBuilder);
		}
        return bulkRequest.execute().actionGet();

	}

	public static BulkResponse indexJsonData(List<ESIndexRequest> esIndexRequests) 
			throws ElasticsearchException, UnknownHostException{
		return indexJsonData(ESClient.getClient(), esIndexRequests);
	}
		
	public static BulkResponse indexJsonData(Client client, List<ESIndexRequest> esIndexRequests) 
			throws ElasticsearchException, UnknownHostException{
		BulkRequestBuilder bulkRequest = client.prepareBulk();
        for(ESIndexRequest indexRequest:esIndexRequests){
        	bulkRequest.add(client.prepareIndex(indexRequest.getIndexName(), indexRequest.getType(),
        			indexRequest.getId()).setSource(indexRequest.getJsonData()));
        }
        return bulkRequest.execute().actionGet();

	}

	public static BulkResponse delete(ESDeleteRequest esDeleteRequest)
			throws ElasticsearchException, UnknownHostException {
		return delete(ESClient.getClient(), esDeleteRequest);
	}

	public static BulkResponse delete(Client client, ESDeleteRequest esDeleteRequest)
			throws ElasticsearchException, UnknownHostException {
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		List<String> deleteIds = esDeleteRequest.getIds();
		String indexName = esDeleteRequest.getIndexName();
		String type = esDeleteRequest.getType();
		for (String id : deleteIds) {
			bulkRequest.add(client.prepareDelete(indexName, type, id));
		}
		return bulkRequest.execute().actionGet();
	}
	
	public static SearchResponse search(ESSearchRequest esSearchRequest) throws ElasticsearchException, UnknownHostException{
		return search(ESClient.getClient(), esSearchRequest);
	}

	public static SearchResponse search(Client client, ESSearchRequest esSearchRequest) throws ElasticsearchException, UnknownHostException{
		QueryBuilder queryBuilder=esSearchRequest.getQueryBuilder();
		FilterBuilder filterBuilder = esSearchRequest.getFilterBuilder();
		if(filterBuilder != null){
			queryBuilder=new FilteredQueryBuilder(queryBuilder, filterBuilder);		
		}
		SearchRequestBuilder searchRequestBuilder=client.prepareSearch(esSearchRequest.getIndexName())
        .setTypes(esSearchRequest.getType())
        .setSearchType(esSearchRequest.getSearchType())
        .setQuery(queryBuilder)
        .setFetchSource(false).setExplain(esSearchRequest.isExplain())
        .setFrom(esSearchRequest.getStart()).setSize(esSearchRequest.getCount());
		if(esSearchRequest.getStart() > esSearchRequest.getMaxStart()){
			searchRequestBuilder.setFrom(esSearchRequest.getMaxStart());
		}
		if(esSearchRequest.getCount() > esSearchRequest.getMaxCount()){
			searchRequestBuilder.setSize(esSearchRequest.getMaxCount());
		}
		if(esSearchRequest.getCount() < 0){
			searchRequestBuilder.setSize(10);
		}
		if(esSearchRequest.getStart() < 0){
			searchRequestBuilder.setFrom(0);
		}
		List<String> fieldList=esSearchRequest.getFields();
		if(fieldList!=null && fieldList.size()>0){
			String[] fields=new String[fieldList.size()];
			searchRequestBuilder.addFields(fieldList.toArray(fields));
		}
		QueryBuilder aggregationQueryBuilder=esSearchRequest.getAggregationQueryBuilder();
		List<String> facetFieldList=esSearchRequest.getFacet_fields();
		if(facetFieldList != null && facetFieldList.size() > 0){
			if(aggregationQueryBuilder!=null){
				org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder aggregationFilterBuilder =
						AggregationBuilders.filter("filter").filter(FilterBuilders.queryFilter(aggregationQueryBuilder));
				org.elasticsearch.search.aggregations.bucket.global.GlobalBuilder aggregationBuilder = AggregationBuilders.global("global")
						.subAggregation(aggregationFilterBuilder);
				searchRequestBuilder.addAggregation(aggregationBuilder);
				for(String facetField:facetFieldList){
					aggregationFilterBuilder.subAggregation(AggregationBuilders.terms(facetField).field(facetField).minDocCount(1).size(500));
				}
			}else {
				for (String facetField : facetFieldList) {
					searchRequestBuilder.addAggregation(new TermsBuilder(facetField).field(facetField).minDocCount(1).size(500));
				}
			}
		}
		List<SortBuilder> sortBuilderList = esSearchRequest.getSortBuilderList();
		if(sortBuilderList != null && sortBuilderList.size() > 0){
			for(SortBuilder sortBuilder : sortBuilderList){
				searchRequestBuilder.addSort(sortBuilder);
			}
		}
		return searchRequestBuilder.execute().actionGet();
	}

	public static void createIndex(String indexName,String mapping) throws Exception{
		createIndex(ESClient.admin_url, indexName, mapping);
	}

	public static void createIndex(String adminUrl,String indexName,String mapping) throws Exception{
		HttpClientUtil.doPut(adminUrl+"/"+indexName, mapping);
		logger.info("createIndex "+ adminUrl + " "+indexName);
	}

	public static void deleteIndex(String indexName) throws Exception{
		deleteIndex(ESClient.admin_url, indexName);
	}
	
	public static void deleteIndex(String adminUrl,String indexName) throws Exception{
		HttpClientUtil.doDelete(adminUrl+"/"+indexName);
		logger.info("deleteIndex "+ adminUrl + " "+indexName);

	}

	public static void updateIndexAlias(String adminUrl,String actionJson) throws JSONException, Exception{
		HttpClientUtil.doPost(adminUrl+"/_aliases", actionJson);
	}

	public static List<String> getIndexNameByAlias(String alias) throws Exception{
		return getIndexNameByAlias(ESClient.admin_url, alias);
	}
	
	public static List<String> getIndexNameByAlias(String adminUrl,String alias) throws Exception{
		try{
			JSONObject jsonObject=HttpClientUtil.doGet2JSONObject(adminUrl+"/_alias/"+alias);
			if(jsonObject!=null){
				return new ArrayList<>(jsonObject.keySet());
			}
		}catch(Exception e){
			logger.warn(e.getMessage(), e);
		}
		return null;

	}

	public static void updateAlias(String indexName,String alias) throws Exception{
		updateAlias(ESClient.admin_url, indexName, alias);
	}

	public static void updateAlias(String adminUrl,String indexName,String alias) throws Exception{
	    JsonObject aliasJsonObject=new JsonObject();
	    aliasJsonObject.addProperty("index", indexName);
	    aliasJsonObject.addProperty("alias", alias);
	    JsonObject addAliasJsonObject=new JsonObject();
	    addAliasJsonObject.add("add", aliasJsonObject);
	    JsonArray jsonArray=new JsonArray();
//	    jsonArray.add(addAliasJsonObject);
	    Collection<String> indexNames=getIndexNameByAlias(adminUrl,alias);
	    if(indexNames!=null&&indexNames.size()>0){
	    	for(String oldName:indexNames){
	    		if(indexNames.size() == 1 && StringUtils.isNotBlank(oldName) && oldName.equals(indexName)){
	    			return;
	    		}
	    		JsonObject aliasJsonObject2=new JsonObject();
			    aliasJsonObject2.addProperty("index", oldName);
			    aliasJsonObject2.addProperty("alias", alias);
			    JsonObject removeAliasJsonObject=new JsonObject();
			    removeAliasJsonObject.add("remove", aliasJsonObject2);
			    jsonArray.add(removeAliasJsonObject);
	    	}
	    }
	    // updateAlias  先 remove 后 add
	    jsonArray.add(addAliasJsonObject);
	    JsonObject actionJsonObject=new JsonObject();
	    actionJsonObject.add("actions", jsonArray);
	    String json=actionJsonObject.toString();
	    logger.info(json + " es cluster adminUrl is " + adminUrl);
	    updateIndexAlias(adminUrl, json);
	}

	public static JSONObject getMappingJsonByAlias(String alias) throws Exception{
		return getMappingJsonByAlias(ESClient.admin_url, alias);
	}
	
	public static JSONObject getMappingJsonByAlias(String adminUrl,String alias) throws Exception{
		Collection<String> indexNames=getIndexNameByAlias(adminUrl,alias);
		if(indexNames!=null&&indexNames.size()>0){
			String indexName=new ArrayList<String>(indexNames).get(0);
			String url=adminUrl+"/"+indexName+"/_mapping";
			return HttpClientUtil.doGet2JSONObject(url);
		}
		return null;
	}
	
	public static List<String> getIndexNamesByPrefix(String indexNamePrefix) throws Exception{
		return getIndexNamesByPrefix(ESClient.admin_url, indexNamePrefix);
	}

	public static List<String> getIndexNamesByPrefix(String adminUrl,String indexNamePrefix) throws Exception{
		String url=adminUrl+"/"+indexNamePrefix+"*";
		JSONObject jsonObject=HttpClientUtil.doGet2JSONObject(url);
		if(jsonObject!=null){
			return new ArrayList<>(jsonObject.keySet());
		}

		return null;
	}
	
	public static Map<String,String> getAllIndexName(String adminUrl) throws Exception{
		Map<String,String> map = new HashMap<>();
		String response = HttpClientUtil.doGet(adminUrl + "/_cat/indices");
		if(StringUtils.isBlank(response)){
			return map;
		}
		response = response.replaceAll(" + ", " ");
		String[] strArray = response.split("\n");
		for(String str : strArray){
			String[] temp = str.split(" ");
			String indexName = temp[2];
			String state = temp[1];
			if(StringUtils.isBlank(indexName) || StringUtils.isBlank(state)){
				continue;
			}
			map.put(indexName, state);
		}
		return map;
	}
	
	
	public static List<String> getClosedIndexNamesByPrefix(String indexNamePrefix) throws Exception{
		return getClosedIndexNamesByPrefix(ESClient.admin_url, indexNamePrefix);
	}
	
	public static List<String> getClosedIndexNamesByPrefix(String adminUrl,String indexNamePrefix) throws Exception{
		List<String> closedIndexNameList = new ArrayList<String>();
		Map<String,String> indexMap = getAllIndexName(adminUrl);
		if(indexMap != null && indexMap.size() > 0){
			for(Map.Entry<String, String> entry : indexMap.entrySet()){
				String indexName = entry.getKey();
				String state = entry.getValue();
				if(indexName.startsWith(indexNamePrefix) && ESSettingConstants.CLOSE.equals(state)){
					closedIndexNameList.add(indexName);
				}
			}
		}
		return closedIndexNameList;
	}

	public static boolean indexExists(String index_name) throws Exception{
		return indexExists(ESClient.getClient(), index_name);
	}
	
	public static boolean indexExists(Client client, String index_name) throws Exception{
		IndicesExistsResponse indicesExistsResponse = client.admin().indices()
						.exists(new IndicesExistsRequest(new String[]{index_name})).actionGet();
		return indicesExistsResponse.isExists();
	}
	
	public static ExistsResponse exist(ESSearchRequest esSearchRequest) throws ElasticsearchException, UnknownHostException{
		return exist(ESClient.getClient(), esSearchRequest);
	}

	public static ExistsResponse exist(Client client, ESSearchRequest esSearchRequest) 
			throws ElasticsearchException, UnknownHostException{
		ExistsRequestBuilder existsRequestBuilder = client.prepareExists(esSearchRequest.getIndexName())
				.setTypes(esSearchRequest.getType())
		        .setQuery(esSearchRequest.getQueryBuilder());
		return existsRequestBuilder.execute().actionGet();
	}

	public static CountResponse count(ESSearchRequest esSearchRequest) throws ElasticsearchException, UnknownHostException {
		return count(ESClient.getClient(), esSearchRequest);
	}
	
	public static CountResponse count(Client client, ESSearchRequest esSearchRequest) 
			throws ElasticsearchException, UnknownHostException {
		CountRequestBuilder countRequestBuilder = client.prepareCount(esSearchRequest.getIndexName())
							.setTypes(esSearchRequest.getType())
							.setQuery(esSearchRequest.getQueryBuilder());

		return countRequestBuilder.execute().actionGet();

	}
	
	public static SuggestResponse suggest(ESSuggestRequest esSuggestRequest) throws ElasticsearchException, UnknownHostException {
		return suggest(ESClient.getClient(), esSuggestRequest);
	}

	public static SuggestResponse suggest(Client client, ESSuggestRequest esSuggestRequest) 
			throws ElasticsearchException, UnknownHostException {
		SuggestRequestBuilder suggestRequestBuilder = client.prepareSuggest(esSuggestRequest.getIndexName());
		CompletionSuggestionBuilder completionSuggestionBuilder = SuggestBuilders.completionSuggestion(esSuggestRequest.getType())
										.text(esSuggestRequest.getInput())
										.field(esSuggestRequest.getField())
										.size(esSuggestRequest.getCount());
		Map<String,List<String>> categoryMap = esSuggestRequest.getCategoryMap();
		if(categoryMap != null && categoryMap.size() > 0){
			for(Map.Entry<String,List<String>> entry : categoryMap.entrySet()){
				String name = entry.getKey();
				List<String> values = entry.getValue();
				if(org.apache.commons.lang.StringUtils.isNotBlank(name)
						&& CollectionUtils.isNotEmpty(values)){
					completionSuggestionBuilder.addCategory(name,values);
				}

			}
		}
		suggestRequestBuilder.addSuggestion(completionSuggestionBuilder);

		return suggestRequestBuilder.execute().actionGet();

	}

	//我们不需要把这些参数暴露出去
	public static final String DEFAULT_SPELLCHECK_NAME = "spellcheck";
	private static final int DEFAULT_MIN_WORD_LENGTH = 2;
	private static final int DEFAULT_TERM_SUGGESTION_SIZE = 3;
	private static final int DEFAULT_MAX_EDIT_DISTANCE = 2;
	private static final int DEFAULT_MIN_DOC_FREQ = 1;

	/**
	 * 使用es的拼写检查功能。
	 *
	 * @param esRequest
	 * @return
	 * @throws ElasticsearchException
	 * @throws UnknownHostException
	 *
	 * @see {@link http://192.168.1.181:8090/pages/viewpage.action?pageId=3473492}
	 */
	public static SuggestResponse spellcheck(ESSpellCheckRequest esRequest) throws ElasticsearchException, UnknownHostException {
		return spellcheck(ESClient.getClient(), esRequest);
	}
	
	public static SuggestResponse spellcheck(Client client, ESSpellCheckRequest esRequest) 
			throws ElasticsearchException, UnknownHostException {
		SuggestRequestBuilder request = client.prepareSuggest(esRequest.getIndexName());

		TermSuggestionBuilder builder = SuggestBuilders.termSuggestion(DEFAULT_SPELLCHECK_NAME);
		builder.text(esRequest.getInput())
			   .field(esRequest.getField())
			   .minWordLength(DEFAULT_MIN_WORD_LENGTH)
			   .size(DEFAULT_TERM_SUGGESTION_SIZE)
			   .maxEdits(DEFAULT_MAX_EDIT_DISTANCE)
			   .minDocFreq(DEFAULT_MIN_DOC_FREQ);

		request.addSuggestion(builder);

		return request.execute().actionGet();
	}
	/**
	 * 更新index的settings配置
	 * @param esIndexSettingUpdateRequest
	 * @return
	 * @throws Exception
	 * @throws UnknownHostException
	 */
	public static UpdateSettingsResponse updateIndexSettings(ESIndexSettingUpdateRequest esIndexSettingUpdateRequest) throws Exception, UnknownHostException{
		return updateIndexSettings(ESClient.getClient(),esIndexSettingUpdateRequest);
	}
	
	public static UpdateSettingsResponse updateIndexSettings(Client client,ESIndexSettingUpdateRequest esIndexSettingUpdateRequest){
		
		Settings settings = ImmutableSettings.settingsBuilder()
							.put(esIndexSettingUpdateRequest.getData())
							.build();
		UpdateSettingsRequestBuilder updateSettingsRequestBuilder = client.admin().indices()
							.prepareUpdateSettings(esIndexSettingUpdateRequest.getIndexName())
							.setSettings(settings);
		
		return updateSettingsRequestBuilder.execute().actionGet();
	}
	
	public static OpenIndexResponse openIndices(List<String> indexNameList) throws ElasticsearchException, UnknownHostException{
		return openIndices(ESClient.getClient(),indexNameList);
	}
	
	public static OpenIndexResponse openIndices(Client client,List<String> indexNameList){
		String[] indices = (String[])indexNameList.toArray(new String[indexNameList.size()]);
		OpenIndexRequestBuilder openIndexRequestBuilder = client.admin().indices()
							.prepareOpen(indices);
		return openIndexRequestBuilder.execute().actionGet();
	}
	
	public static CloseIndexResponse closeIndices(List<String> indexNameList) throws ElasticsearchException, UnknownHostException{
		return closeIndices(ESClient.getClient(),indexNameList);
	}
	
	public static CloseIndexResponse closeIndices(Client client,List<String> indexNameList){
		String[] indices = (String[])indexNameList.toArray(new String[indexNameList.size()]);
		CloseIndexRequestBuilder closeIndexRequestBuilder = client.admin().indices()
							.prepareClose(indices);
		return closeIndexRequestBuilder.execute().actionGet();
	}

	public static void geoIndex(Client client){

	}
	public static SearchResponse search(ESAggSearchRequest esSearchRequest)
			throws ElasticsearchException, UnknownHostException
	{
		QueryBuilder queryBuilder = esSearchRequest.getQueryBuilder();
		FilterBuilder filterBuilder = esSearchRequest.getFilterBuilder();
		if (filterBuilder != null) {
			queryBuilder = new FilteredQueryBuilder(queryBuilder, filterBuilder);
		}
		SearchRequestBuilder searchRequestBuilder = ESClient.getClient().prepareSearch(esSearchRequest.getIndexNames()).setTypes(new String[] { esSearchRequest.getType() }).setSearchType(esSearchRequest.getSearchType()).setQuery(queryBuilder).setExplain(esSearchRequest.isExplain()).setFrom(esSearchRequest.getStart()).setSize(esSearchRequest.getCount());

		List fieldList = esSearchRequest.getFields();
		if ((fieldList != null) && (fieldList.size() > 0)) {
			String[] fields = new String[fieldList.size()];
			searchRequestBuilder.addFields((String[])fieldList.toArray(fields));
		}

		List facetFieldList = esSearchRequest.getFacet_fields();
		if ((facetFieldList != null) && (facetFieldList.size() > 0)) {
			for (Object facetField: facetFieldList) {
				searchRequestBuilder.addAggregation(((TermsBuilder)new TermsBuilder((String) facetField).field((String) facetField)).minDocCount(1L));
			}

		}

		List sortBuilders = esSearchRequest.getSortBuilders();
		if (sortBuilders != null) {
			for (Object sortBuilder : sortBuilders) {
				searchRequestBuilder.addSort((SortBuilder)sortBuilder);
			}
		}
		List aggBuilders = esSearchRequest.getAggrBuilders();
		if ((aggBuilders != null) && (aggBuilders.size() > 0)) {
			for (Object aggregationBuilder : aggBuilders) {
				searchRequestBuilder.addAggregation((AbstractAggregationBuilder)aggregationBuilder);
			}
		}
		return (SearchResponse)searchRequestBuilder.execute().actionGet();
	}

	public static void main(String[] args) throws Exception{
		Client client=ESClient.getClient("ody-lyf-branches-cluster","192.168.8.17:9300");
		List<String> indices = new ArrayList<>();
		indices.add("b2c_2016-11-04_07-48-24");
		indices.add("b2c_2016-11-04_07-48-24");

		
//		System.out.println(closeIndices(client,indices));;
//		System.out.println(openIndices(client,indices));;

//		deleteIndex("http://192.168.6.27:25556", "b2c_2016-11-04_07-48-24");
//		System.out.println(ESService.getIndexNamesByPrefix("http://192.168.6.27:25556", "b2c_"));
		System.out.println("-----------");
//		System.out.println(getIndexNameByAlias("http://192.168.6.27:25556", "old_b2c_alias"));
		/*Map<String,String> map = getAllIndexName("http://192.168.6.27:25556");
		System.out.println(map.size());
		for(Map.Entry<String, String> entry : map.entrySet()){
			System.out.println(entry.getKey()+":" +entry.getValue());

		}*/
//		System.out.println(getClosedIndexNamesByPrefix("http://search-backend.oudianyun.com/ds/search-es","suggest_"));

		ESSuggestRequest suggestRequest = new ESSuggestRequest("suggest_alias","suggest","A_-1__bj");
		suggestRequest.setField("suggest");
		System.out.println(suggest(client,suggestRequest).getSuggest().getSuggestion("suggest").getEntries().get(0).getOptions().get(0).getText());
//		System.out.println(getAllIndexName("http://search-backend.oudianyun.com/ds/search-es"));

		/*List<String> facetFields=new ArrayList<>();
		facetFields.add("brandId_search");
		facetFields.add("attrValueId_search");
		facetFields.add("navCategoryId_search");
		ESSearchRequest esSearchRequest=new ESSearchRequest("b2c_alias","mp");
		esSearchRequest.setExplain(true);
//		esSearchRequest.setFacet_fields(facetFields);
		QueryBuilder queryBuilder=new TermQueryBuilder("tag_words",1);
		esSearchRequest.setQueryBuilder(queryBuilder);
		QueryBuilder queryBuilder2=new TermQueryBuilder("tag_words",2);
        esSearchRequest.setAggregationQueryBuilder(queryBuilder2);
		String token = "秋季";
		esSearchRequest.setCount(400);

		BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();
		boolQueryBuilder.must(new TermQueryBuilder("managementState", 1));
//		boolQueryBuilder.must(new TermQueryBuilder("brandId_search", 8));
		
		DisMaxQueryBuilder disMaxQueryBuilder = new DisMaxQueryBuilder()
				.add(new ConstantScoreQueryBuilder(new TermQueryBuilder("tag_words",token)))
				.add(new ConstantScoreQueryBuilder(new TermQueryBuilder("categoryName_search",token)).boost(200.0f))
				;
		DisMaxQueryBuilder disMaxQueryBuilder = new DisMaxQueryBuilder()
				.add(new TermQueryBuilder("tag_words",token))
				.add(new TermQueryBuilder("categoryName_search",token).boost(200.0f))
				;
//		boolQueryBuilder.must(disMaxQueryBuilder);
		esSearchRequest.setQueryBuilder(boolQueryBuilder);
		
		List<SortBuilder> sortBuilderList = new ArrayList<>();
		sortBuilderList.add(new FieldSortBuilder("id").order(SortOrder.ASC));
		esSearchRequest.setSortBuilderList(sortBuilderList);
		SearchResponse searchResponse=search(client,esSearchRequest);
//		System.out.println(searchResponse);
		for(SearchHit searchHit : searchResponse.getHits().getHits()){
			System.out.println(searchHit.getId());
		}*/
		
		/*BoolQueryBuilder tokenQueryBuilder=new BoolQueryBuilder();
		tokenQueryBuilder.should(new TermQueryBuilder("tag_words",token));
		tokenQueryBuilder.should(new TermQueryBuilder("categoryName_search",token).boost(200.0f));
		boolQueryBuilder.must(tokenQueryBuilder);
		esSearchRequest.setQueryBuilder(boolQueryBuilder);
		SearchResponse searchResponse=search(client,esSearchRequest);
		System.out.println(searchResponse);*/
		
		/*Map<String,String> map = new HashMap<>();
		map.put(ESSettingConstants.NUM_OF_REPLICAS, "2");
		ESIndexSettingUpdateRequest updateRequest = new ESIndexSettingUpdateRequest("b2c_2016-10-21_13-40-21",map);
		System.out.println(updateIndexSettings(client,updateRequest));;*/
	}
}
