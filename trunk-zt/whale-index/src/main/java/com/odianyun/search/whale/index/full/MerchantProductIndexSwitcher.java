package com.odianyun.search.whale.index.full;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.odianyun.search.whale.index.api.common.MerchantAreaIndexContants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.indices.IndexMissingException;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.data.dao.MerchantProductDao;
import com.odianyun.search.whale.data.model.MerchantProductSearch;
import com.odianyun.search.whale.data.model.UpdateTimeRange;
import com.odianyun.search.whale.data.service.ConfigService;
import com.odianyun.search.whale.es.api.ESService;
import com.odianyun.search.whale.es.api.EsIndexSwitcher;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.index.common.ValidateQueryBuilder;
import com.odianyun.search.whale.index.realtime.MerchantProductIncIndex;

public class MerchantProductIndexSwitcher extends EsIndexSwitcher{
	
	static Logger logger = Logger.getLogger(MerchantProductIndexSwitcher.class);
	public static String KEYWORD = "keyword";
	public static String CATEGORY = "category";
	SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat indexDateFormat=new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
	
	@Autowired
	MerchantProductIncIndex merchantProductIncIndex;
	
	@Autowired
	MerchantProductDao productMerchantDao;
	
	@Autowired
	ConfigService configService;
	
	public boolean validate(Client client, String index_name, String index_type, String index_version) throws Exception{
        String fullIndexName = index_name + "_" + index_version;
        String indexNameAlias = index_name + "_alias";
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
		logger.info(clusterName + " index " + fullIndexName + " index_type " + index_type +" has doc num : " + newIndexDocCount);

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
		logger.info(clusterName + " index " + indexNameAlias + " index_type " + index_type + " has doc num : " + nowIndexDocCount);

		exists = validation(nowIndexDocCount,newIndexDocCount);
		logger.info(clusterName + " index " + fullIndexName +" validation: " + exists);
		
		if(!exists){
			return exists;
		}

		if(MerchantAreaIndexContants.index_type.equals(index_type)){
			return exists;
		}
		
		double keywordFailedRatio = configService.getDouble("keywordFailedRatio", 0.1,IndexConstants.DEFAULT_COMPANY_ID);
//		double keywordFailedRatio = 0.1;
		double categoryFailedRatio = configService.getDouble("categoryFailedRatio", 0.1,IndexConstants.DEFAULT_COMPANY_ID);
//		double categoryFailedRatio = 0.1;

		
//		String prePath = configService.get("validationPath", "/data/env/search");
		String keywords = configService.get("validationKeywords", "女装",IndexConstants.DEFAULT_COMPANY_ID);
//		String keywords = "本来便利";
		String categories = configService.get("validationCategories", "4",IndexConstants.DEFAULT_COMPANY_ID);
//		String categories ="578";
		double keywordRatio = validateRatio(client, index_name,index_type,index_version,KEYWORD,keywords);
		double categoryRatio = validateRatio(client, index_name,index_type,index_version,CATEGORY,categories);

		if(keywordRatio > keywordFailedRatio || categoryRatio > categoryFailedRatio){
			logger.info("keywordFailedRatio: " + keywordRatio + "  categoryFailedRatio: " +categoryRatio);
			exists = false;
		}
		return exists;
	
	}
	
	private Boolean validation(long now_count,long new_count) throws Exception{
		if(now_count==0){
			return true;
		}
		double dbRatio = configService.getDouble("dbRatio", 0.8,IndexConstants.DEFAULT_COMPANY_ID);
//		double dbRatio =0.8;
		
		BigDecimal new_count_BigDecimal=new BigDecimal(new_count);
		BigDecimal now_count_BigDecimal=new BigDecimal(now_count);
		double ratio=new_count_BigDecimal.divide(now_count_BigDecimal,new MathContext(2)).doubleValue();
		logger.info("dbRatio=" + dbRatio + " ratio=" + ratio);
		if(ratio<dbRatio){
			return false;
		}
		return true;
	}

	private double validateRatio(Client client, String index_name, String index_type, String index_version,
                                 String searchType, String keywords) throws Exception{
        String fullIndexName = index_name + "_" + index_version;
		String indexNameAlias = index_name + "_alias";
		int failed = 0;
		int size = 0;
//		java.io.BufferedReader br = IOUtils.toBufferedReader(new InputStreamReader(new FileInputStream(new File(path))));
//		String line = br.readLine();
		if(StringUtils.isBlank(keywords)){
			return 0;
		}
		String lines[] = keywords.split(",");
		for(String line : lines){
			size ++;
			ESSearchRequest esSearchRequest = new ESSearchRequest(fullIndexName,index_type);
			
			ValidateQueryBuilder.build(esSearchRequest,line,searchType);
					
			SearchResponse searchRespoonse = ESService.search(client, esSearchRequest);
			SearchHits searchHits = searchRespoonse.getHits();

			esSearchRequest.setIndexName(indexNameAlias);
			SearchResponse searchRespoonse2 = ESService.search(client, esSearchRequest);
			SearchHits searchHits2 = searchRespoonse2.getHits();
			
			if(!compare(searchHits,searchHits2,searchType)){
				failed ++;
			}
		}
		if(size == 0){
			return 0;
		}
			
		return failed/size;
	}

	private boolean compare(SearchHits searchHits, SearchHits searchHits2, String type) {
		boolean validate = false;
		long total = searchHits.getTotalHits();
		long total2 = searchHits2.getTotalHits();
		double keywordRatio = configService.getDouble("keywordRatio", 0.8, IndexConstants.DEFAULT_COMPANY_ID);
		double categoryRatio = configService.getDouble("categoryRatio", 0.8, IndexConstants.DEFAULT_COMPANY_ID);
		
//		double keywordRatio = 0.8;
//		double categoryRatio = 0.8;

		if(total2 == 0){
			return true;
		}
		if(KEYWORD.equals(type)){
			validate = ((double)total / total2) >= keywordRatio ? true : false;
		}else if(CATEGORY.equals(type)){
			validate = ((double)total / total2) >= categoryRatio ? true : false;
		}
		
		return validate;
	}

	@Override
	public void indexAllDataWithPage(String indexName) throws Exception{
		
	}
	
	@Override
	public void incIndexRemediation(String start_index_time,String indexName,List<Integer> companyIds) throws Exception {
		/*logger.info("incIndexRemediation start===================");

		Date start = indexDateFormat.parse(start_index_time);

		if(CollectionUtils.isNotEmpty(companyIds)){
			for(Integer companyId : companyIds){
				//扩大扫描时间段  向前5分钟   向后5分钟
				Long timeSpan = configService.getLong("TimeSpan", 5 * 60 * 1000,companyId);
				start_index_time = simpleDateFormat.format(new Date(start.getTime() - timeSpan));
				
				String end = simpleDateFormat.format(new Date());
				
				int remediationTimes = configService.getInt("RemediationTimes", 2,companyId);
				for(int i = 0 ;i < remediationTimes; i++){
					UpdateTimeRange updateTimeRange = new UpdateTimeRange(start_index_time, end);
					List<Long> merchantProductIds = productMerchantDao.queryProductMerchantIdsByUpdateTime(updateTimeRange,companyId);
					logger.info("companyId: " + companyId +" "+updateTimeRange+"incIndexRemediation update ids="+merchantProductIds);
					if(CollectionUtils.isNotEmpty(merchantProductIds)){
						merchantProductIncIndex.process(merchantProductIds, true, indexName, IndexConstants.index_type,companyId);
					}
					start_index_time = end;
					end = simpleDateFormat.format(new Date());
				}
				
			}
		}
		logger.info("incIndexRemediation end====================");*/

	}
	
    @Override
    public boolean validate(String s) throws Exception {
        return false;
    }
	
	public static void main(String[] args) throws Exception{
		MerchantProductSearch merchantProductSearch=new MerchantProductSearch();
		merchantProductSearch.setId(1L);
		merchantProductSearch.setTag_words("test test");
		merchantProductSearch.setCategoryId(3333L);
		System.out.println(convert(merchantProductSearch));
		
		String mapping_json=IOUtils.toString(MerchantProductIndexSwitcher.class.getResourceAsStream("/es/b2c_mapping.json"));
		ESService.createIndex("b2c_test", mapping_json);
	}
    
}
