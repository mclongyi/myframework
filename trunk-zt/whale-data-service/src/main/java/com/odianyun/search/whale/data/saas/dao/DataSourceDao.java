package com.odianyun.search.whale.data.saas.dao;

import java.util.List;

import com.odianyun.search.whale.data.saas.model.DataSourceConfig;

public interface DataSourceDao {
	
	
	public List<DataSourceConfig> queryAllDataSourceConfig();

}
