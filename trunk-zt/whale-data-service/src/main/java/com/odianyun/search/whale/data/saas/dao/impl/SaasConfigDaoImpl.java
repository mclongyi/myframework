package com.odianyun.search.whale.data.saas.dao.impl;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.odianyun.search.whale.data.saas.dao.SaasConfigDao;
import com.odianyun.search.whale.data.saas.model.CommonConfig;
import com.odianyun.search.whale.data.saas.model.DumpConfig;
import com.odianyun.search.whale.data.saas.model.ESClusterConfig;

public class SaasConfigDaoImpl extends SqlMapClientDaoSupport implements SaasConfigDao {

	@Override
	public List<DumpConfig> queryAllDumpConfigs() {
		return getSqlMapClientTemplate().queryForList("queryAllDumpConfig");
	}

	@Override
	public List<CommonConfig> queryAllCommonConfigs() {
		return getSqlMapClientTemplate().queryForList("queryAllCommonConfig");
	}

	@Override
	public List<ESClusterConfig> queryAllESClusterConfigs() {
		return getSqlMapClientTemplate().queryForList("queryAllESClusterConfig");
	}

}
