package com.odianyun.search.whale.data.saas.dao.impl;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.odianyun.search.whale.data.saas.dao.DataSourceDao;
import com.odianyun.search.whale.data.saas.model.DataSourceConfig;

public class DataSourceDaoImpl extends SqlMapClientDaoSupport implements DataSourceDao{

	@Override
	public List<DataSourceConfig> queryAllDataSourceConfig() {
		// TODO Auto-generated method stub
		return getSqlMapClientTemplate().queryForList("queryAllDataSourceConfig");
	}

}
