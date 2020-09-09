package com.odianyun.search.whale.data.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.odianyun.search.whale.data.common.ResultConvertor;
import com.odianyun.search.whale.data.common.ServiceConstants;
import com.odianyun.search.whale.data.dao.AreaDao;
import com.odianyun.search.whale.data.model.Area;
import com.odianyun.search.whale.data.saas.model.DBType;
import com.odianyun.search.whale.data.saas.service.BaseDaoService;
import com.odianyun.search.whale.data.saas.service.CompanySqlService;

public class AreaDaoImpl extends SqlMapClientDaoSupport implements AreaDao {

	/*@Autowired
	BaseDaoService baseDaoService;
	
	@Autowired
	CompanySqlService companySqlService;*/
	
	@Override
	public List<Area> queryAllAreas(int companyId) throws Exception {
		/*String sql = companySqlService.getSql(companyId, "queryAllAreas");
		Map<String,Object> params = new HashMap<>();
		params.put(ServiceConstants.COMPANYID, companyId);
		List<Map> list = baseDaoService.getBaseDao(companyId, DBType.osc).queryForList(sql,params);
		List<Area> result = new ArrayList<Area>();
		if(CollectionUtils.isNotEmpty(list)){
			result = ResultConvertor.convertFromMap(list, Area.class);
		}
		return result;*/
		Map<String,Object> params = new HashMap<>();
		params.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("queryAllAreas",params);
	}

	@Override
	public List<Area> queryAllAreas() throws Exception {
		Map<String,Object> params = new HashMap<>();
		return getSqlMapClientTemplate().queryForList("queryAllAreas",params);
	}

	@Override
	public List<Area> queryAreasByIds(List<Long> areaIds, int companyId) throws Exception {
		/*String sql = companySqlService.getSql(companyId, "queryAreasByIds");
		Map<String,Object> params = new HashMap<>();
		params.put(ServiceConstants.IDS, areaIds);
		params.put(ServiceConstants.COMPANYID, companyId);
		List<Map> list = baseDaoService.getBaseDao(companyId, DBType.osc).queryForList(sql,params);
		List<Area> result = new ArrayList<Area>();
		if(CollectionUtils.isNotEmpty(list)){
			result = ResultConvertor.convertFromMap(list, Area.class);
		}
		return result;*/
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.IDS, areaIds);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("queryAreasByIds",paramMap);
	}

	@Override
	public List<Area> queryAreasByCodes(List<Long> codes, int companyId) throws Exception {
		/*String sql = companySqlService.getSql(companyId, "queryAreasByCodes");
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.IDS, codes);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		List<Map> list = baseDaoService.getBaseDao(companyId, DBType.osc).queryForList(sql,paramMap);
		List<Area> result = new ArrayList<Area>();
		if(CollectionUtils.isNotEmpty(list)){
			result = ResultConvertor.convertFromMap(list, Area.class);
		}
		return result;*/
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.IDS, codes);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("queryAreasByCodes",paramMap);
	}

}
