package com.odianyun.search.whale.data.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.odianyun.search.whale.data.dao.ConfigDao;
import com.odianyun.search.whale.data.model.Config;

public class ConfigDaoImpl extends SqlMapClientDaoSupport implements ConfigDao {

	@Override
	public List<Config> queryAllConfigData(Long companyId) {
		Map<String, Object> params = new HashMap<String, Object>();
		List<Long> companyIdList = new ArrayList<Long>();
		companyIdList.add(companyId);
		if(!companyIdList.contains(-1l)){
			companyIdList.add(-1l);
		}
		params.put("companyId", companyIdList);
		return this.getSqlMapClientTemplate().queryForList("queryAllConfigData", params);
	}

}
