package com.odianyun.search.whale.data.saas.service;


import java.util.Set;

import com.odianyun.search.whale.data.saas.model.CommonConfig;
import com.odianyun.search.whale.data.saas.model.DumpConfig;
import com.odianyun.search.whale.data.saas.model.ESClusterConfig;

public interface SaasConfigService {
	public Set<String> getAllIndexNames();
	
	public DumpConfig getDumpConfig(String indexName);
	
	public ESClusterConfig getESClusterConfig(int clusterId);
	
	public CommonConfig getCommonConfig(String indexName);
	
	public CommonConfig getCommonConfigByCompany(int companyId);
}
