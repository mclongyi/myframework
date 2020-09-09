package com.odianyun.search.whale.data.saas.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.data.saas.dao.SaasConfigDao;
import com.odianyun.search.whale.data.saas.model.CommonConfig;
import com.odianyun.search.whale.data.saas.model.DumpConfig;
import com.odianyun.search.whale.data.saas.model.ESClusterConfig;
import com.odianyun.search.whale.data.saas.service.SaasConfigService;
import com.odianyun.search.whale.data.service.impl.AbstractDBService;

public class SaasConfigServiceImpl extends AbstractDBService implements SaasConfigService {
	private static Logger logger = Logger.getLogger(SaasConfigServiceImpl.class);
	
	private Map<String, DumpConfig> dumpConfigMap = new HashMap<>();
	private Map<Integer, ESClusterConfig> clusterConfigMap = new HashMap<>();
	private Map<String, CommonConfig> commonConfigMap = new HashMap<>();
	private Map<Integer, CommonConfig> companyCommonConfigMap = new HashMap<>();

	@Autowired
	SaasConfigDao saasConfigDao;

	@Override
	protected void tryReload() throws Exception {
		reloadCommonConfig();
		reloadDumpConfig();
		reloadClusterConfig();
	}

	@Override
	public Set<String> getAllIndexNames() {
		return commonConfigMap.keySet();
	}

	@Override
	public DumpConfig getDumpConfig(String indexName) {
		return dumpConfigMap.get(indexName);
	}

	@Override
	public ESClusterConfig getESClusterConfig(int clusterId) {
		return clusterConfigMap.get(clusterId);
	}

	@Override
	public CommonConfig getCommonConfig(String indexName) {
		return commonConfigMap.get(indexName);
	}

	@Override
	public CommonConfig getCommonConfigByCompany(int companyId) {
		return companyCommonConfigMap.get(companyId);
	}

	private void reloadDumpConfig() {
		List<DumpConfig> dumpConfigs = null;
		try {
			dumpConfigs = saasConfigDao.queryAllDumpConfigs();
		} catch (Exception e) {
			logger.error("dumpConfigDao load db error : " + e);
		}

		if (CollectionUtils.isNotEmpty(dumpConfigs)) {
			Map<String, DumpConfig> tempDumpConfigMap = new HashMap<>();
			for (DumpConfig dumpConfig : dumpConfigs) {
				tempDumpConfigMap.put(dumpConfig.getIndexName(), dumpConfig);
			}
			
			dumpConfigMap = tempDumpConfigMap;
		}
	}

	private void reloadCommonConfig() {
		List<CommonConfig> commonConfigs = new ArrayList<CommonConfig>();
		try {
			commonConfigs = saasConfigDao.queryAllCommonConfigs();
		} catch (Exception e) {
			logger.error("load common config error : " + e);
			return;
		}

		Map<String, CommonConfig> tempCommonConfigMap = new HashMap<>();
		for (CommonConfig commonConfig : commonConfigs) {
			tempCommonConfigMap.put(commonConfig.getIndexName(), commonConfig);
		}
		commonConfigMap = tempCommonConfigMap;
		
		Map<Integer, CommonConfig> tempCompanyCommonConfigMap = new HashMap<>();
		for (CommonConfig commonConfig : commonConfigs) {
			tempCompanyCommonConfigMap.put(commonConfig.getCompanyId(), commonConfig);
		}
		companyCommonConfigMap = tempCompanyCommonConfigMap;
	}
	
	private void reloadClusterConfig() {
		List<ESClusterConfig> clusterConfigs = null;
		try {
			clusterConfigs = saasConfigDao.queryAllESClusterConfigs();
		} catch (Exception e) {
			logger.error("load cluster config error : " + e);
		}

		if (CollectionUtils.isNotEmpty(clusterConfigs)) {
			Map<Integer, ESClusterConfig> tempClusterConfigMap = new HashMap<>();
			for (ESClusterConfig clusterConfig : clusterConfigs) {
				tempClusterConfigMap.put(clusterConfig.getId(), clusterConfig);
			}
			
			clusterConfigMap = tempClusterConfigMap;
		}
	}

	@Override
	public int getInterval() {
		return 10;
	}
	
	@Override
	protected void tryReload(List<Long> ids) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
