package com.odianyun.search.whale.data.saas.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.odianyun.search.whale.data.saas.dao.CompanySqlDao;
import com.odianyun.search.whale.data.saas.model.CompanySqlConfig;

public class CompanySqlDaoImpl extends SqlMapClientDaoSupport implements CompanySqlDao {

	@Override
	public List<CompanySqlConfig> getCompanySqlWithPage(long maxId, int pageSize) {
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("maxId", maxId);
        params.put("pageSize", pageSize);
		return getSqlMapClientTemplate().queryForList("getCompanySqlWithPage", params);
	}

	@Override
	public List<CompanySqlConfig> getAllCompanySql() {
		return getSqlMapClientTemplate().queryForList("getAllCompanySql");
	}

}
