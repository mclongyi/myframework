package com.odianyun.search.whale.data.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.common.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.reflect.TypeToken;
import com.odianyun.search.whale.common.util.ConfigUtil;
import com.odianyun.search.whale.common.util.GsonUtil;
import com.odianyun.search.whale.data.common.ServiceConstants;
import com.odianyun.search.whale.data.dao.ConfigDao;
import com.odianyun.search.whale.data.model.Config;
import com.odianyun.search.whale.data.service.AbstractCompanyDBService;
import com.odianyun.search.whale.data.service.ConfigService;

public class ConfigServiceImpl extends AbstractCompanyDBService implements ConfigService {

	static Logger logger = Logger.getLogger(ConfigServiceImpl.class);

	@Autowired
	ConfigDao configDao;
	
	Map<String,String> configMap = new ConcurrentHashMap<>();
	
	static final int DEFAULT_COMPANY_TAG = -1;

	static String DEFAULT_KEY_PRE = DEFAULT_COMPANY_TAG+"_";
	
	String configFilePath = "";
	String poolName = "index";

	/**
	 * 2016-09-07 暂时不考虑poolName的概念
	 */
	public ConfigServiceImpl(){
		configFilePath = System.getProperty(ServiceConstants.GLOBAL_CONFIG_PATH,"/data/env")
				+"/snapshot/search/"+ poolName+"/" + ConfigUtil.get(ServiceConstants.CONFIG_FILE_NAME, "config.properties");
	}
	
	@Override
	protected void tryReload(int companyId) throws Exception {
		reloadConfigData(companyId);
	}

	private void reloadConfigData(int companyId) throws Exception {
//		logger.info("configFilePath :===================================" + configFilePath);
		List<Config> list = null;
		try{
			list = configDao.queryAllConfigData(new Long(companyId));
		}catch(Exception e){
			logger.error("configDao load db error : " + e);
		}
		if(CollectionUtils.isNotEmpty(list)){
			Map<String,String> tempMap = new HashMap<>();
			for(Config config : list){
				String key = config.getCompanyId() +"_"+ config.getKey();
				tempMap.put(key, config.getValue());
			}
			if(tempMap.size() > 0){
				configMap.putAll(tempMap);
				String json = GsonUtil.getGson().toJson(configMap);
				File file = new File(configFilePath);
				if(!file.exists()){
					file.createNewFile();
				}
				FileUtils.writeStringToFile(file, json, false);
				logger.info("write json : "+ json);
			}
		}else{
			try{
				String json = FileUtils.readFileToString(new File(configFilePath));
				Map<String,String> tempMap = GsonUtil.getGson().fromJson(json,
						new TypeToken<Map<String, String>>() {}.getType());
                logger.info("read json : "+ json);
                if(tempMap != null && tempMap.size() > 0){
    			configMap.putAll(tempMap);
               }
			}catch(Exception e){
				logger.warn(e.getMessage());
			}
			
			
		}
	}

	@Override
	public int getInterval() {
		return 10;
	}

	@Override
	public String get(String configName,int companyId) {
		if(configName == null || configMap.size() == 0){
			return null;
		}
		String key = companyId+"_"+configName;
		String value = configMap.get(key);
		if(StringUtils.isBlank(value)){
			key = DEFAULT_KEY_PRE+configName;
			value = configMap.get(key);
		}
		return value;
	}

	@Override
	public String get(String configName, String def,int companyId) {
		String value = get(configName,companyId);
		return value == null ? def : value;
	}

	@Override
	public Double getDouble(String configName,int companyId) {
		String value = get(configName,companyId);
		return value == null ? 0 : Double.parseDouble(value);
	}

	@Override
	public Double getDouble(String configName, double def,int companyId) {
		String value = get(configName,companyId);
		return value == null ? def : Double.parseDouble(value);
	}

	@Override
	public Long getLong(String configName,int companyId) {
		String value = get(configName,companyId);
		return value == null ? 0 : Long.parseLong(value);
	}

	@Override
	public Long getLong(String configName, long def,int companyId) {
		String value = get(configName,companyId);
		return value == null ? def : Long.parseLong(value);
	}

	@Override
	public Integer getInt(String configName,int companyId) {
		String value = get(configName,companyId);
		return value == null ? 0 : Integer.parseInt(value);
	}

	@Override
	public Integer getInt(String configName, int def,int companyId) {
		String value = get(configName,companyId);
		return value == null ? def : Integer.parseInt(value);
	}

	@Override
	public Boolean getBool(String configName,int companyId) {
		String value = get(configName,companyId);
		return value == null ? false : Boolean.parseBoolean(value);
	}

	@Override
	public Boolean getBool(String configName, boolean def,int companyId) {
		String value = get(configName,companyId);
		return value == null ? def : Boolean.parseBoolean(value);
	}
	

}
