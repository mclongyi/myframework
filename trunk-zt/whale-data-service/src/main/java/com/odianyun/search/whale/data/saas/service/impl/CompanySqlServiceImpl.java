package com.odianyun.search.whale.data.saas.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.elasticsearch.common.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.common.util.ConfigUtil;
import com.odianyun.search.whale.data.common.ServiceConstants;
import com.odianyun.search.whale.data.saas.dao.CompanySqlDao;
import com.odianyun.search.whale.data.saas.model.Company;
import com.odianyun.search.whale.data.saas.model.CompanySqlContext;
import com.odianyun.search.whale.data.saas.service.CompanyService;
import com.odianyun.search.whale.data.saas.service.CompanySqlService;
import com.odianyun.search.whale.data.service.impl.AbstractDBService;

public class CompanySqlServiceImpl extends AbstractDBService implements CompanySqlService{

	static Logger logger = Logger.getLogger(CompanySqlServiceImpl.class);

	private Map<String,CompanySqlContext> companySqlContextMap = new HashMap<>();
	
	private String groupName = ConfigUtil.get(ServiceConstants.GROUP_NAME, ServiceConstants.LATEST);

	private Properties groupProperties;
	
//	@Autowired
//	CompanySqlDao companySqlDao;
	
	@Autowired
	CompanyService companyService;
	
	@Override
	public String getSql(Integer companyId, String sqlName) {
		/*if(companyId != null && companyId != 0){
			*//**
			 * 获取sql的逻辑主要是  首先获取公司的虚拟companyId（如果存在的话，否则就是公司id），根据虚拟id去获取，获取不到时使用默认id
			 *//*
			companyId = companyService.getCompanyIdOrVirtualCompanyId(companyId);
			CompanySqlContext companySqlContext = companySqlContextMap.get(String.valueOf(companyId));
			if(null != companySqlContext){
				Map<String,String> sqlMap = companySqlContext.getSqlMap();
				if(null != sqlMap && sqlMap.containsKey(sqlName)){
					String sql = sqlMap.get(sqlName);
					return sql;
				}else{
					// 获取默认companyId
					return getSql(getDefaultCompanyId(companyId),sqlName);
				}
			}else{
				// 获取默认companyId
				return getSql(getDefaultCompanyId(companyId),sqlName);
			}
		}*/
		String sql = null;
		if(null != companyId && companyId != 0){
			Company company = null;
			if(null == company){
				return null;
			}
			String shortName = company.getShortName();
			Integer virtualCompanyId = company.getVirtualCompanyId();
			companyId = virtualCompanyId == null ? companyId : virtualCompanyId;
			if(null != virtualCompanyId){
				String tempShortName = groupProperties.getProperty(String.valueOf(companyId));
				if(StringUtils.isNotBlank(tempShortName)){
					shortName = tempShortName;
				}
			}
			sql = getSql(shortName,sqlName);
			if(StringUtils.isBlank(sql)){
				sql = getSql(groupName,sqlName);
			}
			if(StringUtils.isBlank(sql)){
				sql = getSql(ServiceConstants.DEFAULT,sqlName);
			}

		}
		if(StringUtils.isBlank(sql)){
			logger.error("companyId : "+ companyId +" getSQL return null!!!");
		}
		return sql;
	}
	
	private String getSql(String shortName,String sqlName){
		CompanySqlContext companySqlContext = companySqlContextMap.get(shortName);
		if(null != companySqlContext){
			return companySqlContext.getProperties().getProperty(sqlName);
		}
		return null;
	}

	@Override
	protected void tryReload() throws Exception {
		boolean loadSQLFromFile = ConfigUtil.getBool("load_sql_from_file", true);
		groupName = ConfigUtil.get(ServiceConstants.GROUP_NAME, ServiceConstants.LATEST);
		if(loadSQLFromFile){
			loadFromFile();
		}else{
//			loadFromDB();
		}
	}

	@Override
	public int getInterval() {
		return 60;
	}
	
	private Integer getDefaultCompanyId(Integer companyId){
		
		return ServiceConstants.DEFAULT_COMPANY_ID;
	}

	@Override
	public String getSql(Integer companyId, String sqlName, Object parameterObject) {
		String sql = getSql(companyId,sqlName);
		if(StringUtils.isNotBlank(sql)){
			
		}
		return null;
	}

	@Override
	protected void tryReload(List<Long> ids) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	/*private void loadFromDB(){
		Map<String,CompanySqlContext> tempCompanySqlContextMap = new HashMap<>();
		List<CompanySqlConfig> sqlList = companySqlDao.getAllCompanySql();
		if(CollectionUtils.isNotEmpty(sqlList)){
			for(CompanySqlConfig companySqlConfig : sqlList){
				Integer companyId = companySqlConfig.getCompanyId();
				CompanySqlContext companySqlContext = tempCompanySqlContextMap.get(String.valueOf(companyId));
				if(companySqlContext == null){
					companySqlContext = new CompanySqlContext();
					tempCompanySqlContextMap.put(String.valueOf(companyId), companySqlContext);
				}
				Map<String,String> sqlMap = companySqlContext.getSqlMap();
				if(sqlMap == null){
					sqlMap = new HashMap<>();
					companySqlContext.setSqlMap(sqlMap);
				}
				sqlMap.put(companySqlConfig.getSqlName(), companySqlConfig.getSqlStr());
			}
		}
		if(tempCompanySqlContextMap.size() > 0){
			this.companySqlContextMap = tempCompanySqlContextMap;
		}
	}*/
	
	private void loadFromFile(){
		//1. 加载group.properties
//		File groupEnvFile = new File(CompanySqlServiceImpl.class.getResource(ServiceConstants.SQL_PATH + ServiceConstants.GROUP_ENV).getFile());
		Properties tempGroupProperties = loadPropertiesFile(ServiceConstants.SQL_PATH +"/"+ ServiceConstants.GROUP_ENV);
		if(tempGroupProperties.isEmpty()){
			logger.warn("there is no properties in "+ ServiceConstants.GROUP_ENV);
		}else{
			this.groupProperties = tempGroupProperties;
		}
		Map<String,CompanySqlContext> tempCompanySqlContextMap = new HashMap<>();

		//2.加载默认sql
//		File defaultSqlFile = new File(CompanySqlServiceImpl.class.getResource(ServiceConstants.SQL_PATH + ServiceConstants.DEFAULT + "/" + ServiceConstants.DEFAULT_SQL_File).getFile());
		
		Properties defaultSql = loadPropertiesFile(ServiceConstants.SQL_PATH +"/"+ ServiceConstants.DEFAULT + "/" + ServiceConstants.DEFAULT_SQL_File);
		if(!defaultSql.isEmpty()){
			CompanySqlContext companySqlContext = new CompanySqlContext();
			companySqlContext.setProperties(defaultSql);
			tempCompanySqlContextMap.put(ServiceConstants.DEFAULT, companySqlContext);
		}
		//3.加载最新的sql版本
//		File latestFile = new File(CompanySqlServiceImpl.class.getResource(ServiceConstants.SQL_PATH + ServiceConstants.LATEST).getFile());
		String latestPath = ServiceConstants.SQL_PATH +"/"+ ServiceConstants.LATEST;
		Map<String,Properties> latestSqlMap = new LinkedHashMap<>();
		/*File[] sqlFiles = latestFile.listFiles();
		if(null != sqlFiles && sqlFiles.length > 0){
			for(File f : sqlFiles){
				Properties properties = loadPropertiesFile(f.getAbsolutePath());
				if(!properties.isEmpty()){
					String name = f.getName().replace(ServiceConstants.PROPERTIE_SUFFIX, "");
					latestSqlMap.put(name, properties);
				}
			}
		}*/
		String latestVersionStr = groupProperties.getProperty(ServiceConstants.LATEST_VERSION);
		
		if(StringUtils.isNotBlank(latestVersionStr)){
			double initVersion = ServiceConstants.INIT_VERSION;
			double latestVersion = Double.parseDouble(latestVersionStr);
			while(initVersion <= latestVersion){
				Properties properties = loadPropertiesFile(latestPath+"/"+latestVersion+ServiceConstants.PROPERTIE_SUFFIX);
				if(!properties.isEmpty()){
					latestSqlMap.put(String.valueOf(initVersion), properties);
				}
				initVersion += ServiceConstants.INTERVAL;
			}
		}

		//4.加载group中的sql
//		File groupFile = new File(CompanySqlServiceImpl.class.getResource(ServiceConstants.SQL_PATH + groupName).getFile());
		if(latestSqlMap.size() > 0){
			loadCompanySqlContext(tempCompanySqlContextMap, groupName,latestSqlMap);
		}
			
		//5.切换缓存
		if(tempCompanySqlContextMap.size() > 0){
			this.companySqlContextMap = tempCompanySqlContextMap;
		}
	}
		
	/*private void loadCompanySqlContext(Map<String, CompanySqlContext> tempCompanySqlContextMap, String key,
			File file, Map<String, Properties> latestSqlMap) {
		if(ServiceConstants.LATEST.equals(key)){
			Properties properties = new Properties();
			for(Entry<String, Properties> entry : latestSqlMap.entrySet()){
				properties.putAll(entry.getValue());
			}
			if(!properties.isEmpty()){
				CompanySqlContext companySqlContext = new CompanySqlContext();
				companySqlContext.setProperties(properties);
				tempCompanySqlContextMap.put(key, companySqlContext);
			}
			return;
		}
		File[] files = file.listFiles();
		if(null != files && files.length > 0){
			for(File f : files){
				if(f.isDirectory()){
					String name = f.getName().replace(ServiceConstants.PROPERTIE_SUFFIX, "");
					loadCompanySqlContext(tempCompanySqlContextMap,name,f,latestSqlMap);
				}else{
					loadCompanySqlContextWithVersion(tempCompanySqlContextMap,key,f,latestSqlMap);
				}
			}
		}
		
	}*/
	
	private void loadCompanySqlContext(Map<String, CompanySqlContext> tempCompanySqlContextMap, String key,
			Map<String, Properties> latestSqlMap) {
		if(ServiceConstants.LATEST.equals(key)){
			Properties properties = new Properties();
			for(Entry<String, Properties> entry : latestSqlMap.entrySet()){
				properties.putAll(entry.getValue());
			}
			if(!properties.isEmpty()){
				CompanySqlContext companySqlContext = new CompanySqlContext();
				companySqlContext.setProperties(properties);
				tempCompanySqlContextMap.put(key, companySqlContext);
			}
			return;
		}
		
		String groupInfo = groupProperties.getProperty(key);
		loadCompanySqlContextWithVersion(tempCompanySqlContextMap,key,ServiceConstants.SQL_PATH + "/" + groupName,latestSqlMap);

		if(StringUtils.isBlank(groupInfo)){
			return;
		}
		String[] companys = groupInfo.split(",");
		if(null != companys && companys.length > 0){
			for(String company : companys){
				loadCompanySqlContextWithVersion(tempCompanySqlContextMap,company,ServiceConstants.SQL_PATH + "/" + groupName +"/" + company,latestSqlMap);
			}
		}
		
	}

	private Properties loadPropertiesFile(String fileName){
		Properties properties = new Properties();
		try {	
			InputStream in = CompanySqlServiceImpl.class.getResourceAsStream(fileName);
			properties.load(in);
		} catch (Exception e) {
			logger.error("loadPropertiesFile error !!! fileName: " + fileName + "  "+ e.getMessage());
		}
		return properties;
	}
	private void loadCompanySqlContextWithVersion(Map<String, CompanySqlContext> companySqlContextMap,String key, String sqlVersionFilePath,Map<String,Properties> latestSqlMap) {

		Properties sqlVersionProperties = loadPropertiesFile(sqlVersionFilePath+"/"+ServiceConstants.SQL_VERSION_FILE);
		if(!sqlVersionProperties.isEmpty()){
			String sqlVersion = sqlVersionProperties.getProperty(ServiceConstants.SQL_VERSION);
			if(StringUtils.isBlank(sqlVersion)){
				return;
			}
			Properties properties = new Properties();
			for(Entry<String, Properties> entry : latestSqlMap.entrySet()){
				properties.putAll(latestSqlMap.get(sqlVersion));
				if(sqlVersion.equals(entry.getKey())){
					break;
				}
			}
			if(!properties.isEmpty()){
				CompanySqlContext companySqlContext = new CompanySqlContext();
				companySqlContext.setProperties(properties);
				companySqlContextMap.put(key, companySqlContext);
			}		
		}
	}

}
