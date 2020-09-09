package com.odianyun.search.whale.data.saas.dao;

import java.util.List;

import com.odianyun.search.whale.data.saas.model.CommonConfig;
import com.odianyun.search.whale.data.saas.model.DumpConfig;
import com.odianyun.search.whale.data.saas.model.ESClusterConfig;

public interface SaasConfigDao {
	
	List<DumpConfig> queryAllDumpConfigs();
	
	List<CommonConfig> queryAllCommonConfigs();
	
	List<ESClusterConfig> queryAllESClusterConfigs();
}
