package com.odianyun.search.whale.es.api;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.misc.IndexMergeTool;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.lang3.StringUtils;


//import com.odianyun.search.whale.common.util.ConfigUtil;
import com.odianyun.search.whale.es.api.ESService;


public abstract class EsIndexSwitcher {


	
	static Logger logger = Logger.getLogger(EsIndexSwitcher.class);
	
	static int DEFAULT_INDEX_NUM = 5;

//	private boolean needValidation = ConfigUtil.getBool("needValidation", false);

	public void createIndex(String start_index_time, String indexName_pre, String mappingName) throws Exception{
		createIndex(ESClient.admin_url,start_index_time,indexName_pre, mappingName);
	}
	
	public void createIndex(String adminUrl, String start_index_time, String indexName_pre, 
			String mappingName) throws Exception{
		String new_index_name = indexName_pre + start_index_time;
		logger.info("new index name is " + new_index_name + " es cluster adminUrl is " + adminUrl);
		String mapping_json = IOUtils.toString(EsIndexSwitcher.class.getResourceAsStream(mappingName));
		ESService.createIndex(adminUrl, new_index_name, mapping_json);
		logger.info("new index " + new_index_name + " create sucessful");
	}

	public void switchIndex(String start_index_time,
			String indexName_pre,String mappingName,String aliasName,boolean needValidation,List<Integer> companyIds,int indexNum) throws Exception{
		switchIndex(ESClient.admin_url, start_index_time, indexName_pre, mappingName, aliasName, needValidation,companyIds,indexNum);
	}
	
	public void switchIndex(String start_index_time,
			String indexName_pre,String mappingName,String aliasName,boolean needValidation,List<Integer> companyIds) throws Exception{
		switchIndex(ESClient.admin_url, start_index_time, indexName_pre, mappingName, aliasName, needValidation,companyIds,DEFAULT_INDEX_NUM);
	}
	
	public void switchIndex(String adminUrl, String start_index_time,
			String indexName_pre,String mappingName,String aliasName,boolean needValidation,List<Integer> companyIds,int indexNum) throws Exception{
		String new_index_name=indexName_pre+start_index_time;
		List<String> indexNames=ESService.getIndexNameByAlias(adminUrl, aliasName);
		String currentIndexName="";
		if(indexNames!=null&&indexNames.size()>0){
			currentIndexName=indexNames.get(0);
		}
		if(StringUtils.isBlank(currentIndexName)){
			logger.info(" alias does not poit to any index "+ currentIndexName);
		}
		logger.info("validate index "+new_index_name+" successfully" + " es cluster adminUrl is " + adminUrl);
		//弥补全量期间的实时索引损失
//		incIndexRemediation(start_index_time,new_index_name,companyIds);

		ESService.updateAlias(adminUrl, new_index_name, aliasName);
		Thread.sleep(10*1000);
		logger.info(aliasName+" alias point to "+ESService.getIndexNameByAlias(adminUrl, aliasName) + " es cluster adminUrl is " + adminUrl);
		// 关闭被切换的索引
		closeOldIndices(currentIndexName);
		// 关闭除了新建索引以外的其他open的索引（历史遗留或者手工open的...）
		closeOldIndices(adminUrl,new_index_name,indexName_pre);
		
		deleteClosedIndexes(adminUrl, new_index_name, indexName_pre, indexNum);
		
//		deleteOldIndexes(adminUrl, new_index_name,indexName_pre,indexNum);
//		closeOldIndices(adminUrl,new_index_name,indexName_pre);
	}
	
	private void closeOldIndices(String currentIndexName) throws Exception {
		if(StringUtils.isBlank(currentIndexName)){
			logger.info(" currentIndexName is blank !!");
			return;
		}
		List<String> indexNames = new ArrayList<String>();
		indexNames.add(currentIndexName);
		ESService.closeIndices(indexNames);
		logger.info(" closeIndices : " + indexNames);
	}
	
	private void closeOldIndices(String adminUrl,String newIndexName, String indexNamePre) throws Exception {
		List<String> indexNames = ESService.getIndexNamesByPrefix(adminUrl, indexNamePre);
		if(CollectionUtils.isEmpty(indexNames)){
			return;
		}
		indexNames.remove(newIndexName);
		if(CollectionUtils.isNotEmpty(indexNames)){
			ESService.closeIndices(indexNames);
			logger.info(" close otherIndices : " + indexNames);
		}
	}

	public abstract void incIndexRemediation(String start_index_time,String indexName,List<Integer> companyId) throws Exception;
	
	public abstract boolean validate(String index_name) throws Exception;

	public abstract void indexAllDataWithPage(String indexName) throws Exception;
	
	public static Map<String,Object> convert(Object obj) throws Exception{
		Map<String,Object> map=new HashMap<String,Object>();
		Field[] fields=obj.getClass().getDeclaredFields();
		for(Field field:fields){
			String propertyName = field.getName();
			String getMethodName="get"+propertyName.substring(0,1).toUpperCase()+propertyName.substring(1);
			Method getMethod=obj.getClass().getMethod(getMethodName, null);
			if(getMethod==null)
            	continue;        
            Object getValue=getMethod.invoke(obj,null);
            if(getValue!=null){
            	map.put(propertyName,getValue);
            }
		}
		return map;
	}
	
	/*private static void deleteOldIndexes(String newIndexName,String preIndexName,String indexName_pre) throws Exception{
		deleteOldIndexes(ESClient.admin_url, newIndexName, preIndexName, indexName_pre,DEFAULT_INDEX_NUM);
	}*/
	
	private static void deleteClosedIndexes(String adminUrl, String newIndexName,
			String indexName_pre, int indexNum) throws Exception{
		List<String> indexNames=ESService.getClosedIndexNamesByPrefix(adminUrl, indexName_pre);
		//保留 INDEX_NUM 份历史索引 1份open  INDEX_NUM-1份close
		indexNum = indexNum - 1;
		if(indexNames != null && indexNames.size() > indexNum){
			Collections.sort(indexNames, new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					return o2.compareTo(o1);
				}
			});
			for(int i = indexNum; i < indexNames.size(); i ++){
				String indexName = indexNames.get(i);
				if(!indexName.equals(newIndexName)){
					ESService.deleteIndex(adminUrl, indexName);
					logger.info(adminUrl + " delete closedIndex : " + indexName);
				}
			}
		}
	}
	
	private static void deleteOldIndexes(String adminUrl, String newIndexName,
			String indexName_pre, int indexNum) throws Exception{
		List<String> indexNames=ESService.getIndexNamesByPrefix(adminUrl, indexName_pre);
		//保留 INDEX_NUM 份历史索引
		if(indexNames != null && indexNames.size() > indexNum){
			Collections.sort(indexNames, new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					return o2.compareTo(o1);
				}
			});
			for(int i = indexNum; i < indexNames.size(); i ++){
				String indexName = indexNames.get(i);
				if(!indexName.equals(newIndexName)){
					ESService.deleteIndex(adminUrl, indexName);
				}
			}
		}
	}
    

}
