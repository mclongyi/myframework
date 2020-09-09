package com.odianyun.search.whale.data.saas.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.odianyun.search.whale.data.common.ResultConvertor;
import com.odianyun.search.whale.data.common.ServiceConstants;
import com.odianyun.search.whale.data.model.UpdateTimeRange;
import com.odianyun.search.whale.data.saas.dao.BaseDao;

public class BaseDaoImpl extends SqlMapClientDaoSupport implements BaseDao {

	private static Pattern supplement = Pattern.compile("(\\w+)\\.\\w+");
	
	private static final String SELECT_BY_SQL_STR_WITH_PARAMS_AND_WHERE = "selectBySqlWithParamsAndWhere";
	private static final String SELECT_BY_SQL_STR_WITH_PARAMS = "selectBySqlWithParams";
	private static final String SELECT_BY_SQL_STR = "selectBySqlStr";
	static Logger logger = Logger.getLogger(BaseDaoImpl.class);

	@Override
	public List queryForList(String sql) {
		return getSqlMapClientTemplate().queryForList(SELECT_BY_SQL_STR, sql);
	}

	@Override
	public List queryForList(String sql, Object paramObject) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.putAll(generateParamMap(paramObject));
		return queryForList(sql,params);
	}
	
	private Map<String,Object> generateParamMap(Object paramObject){
		Map<String, Object> params = new HashMap<String, Object>();
		if(null == paramObject){
			return params;
		}
		if(paramObject instanceof List){
			params.put(ServiceConstants.IDS, (List<Long>)paramObject);
		}else if(paramObject instanceof Long){
			params.put(ServiceConstants.ID, (Long)paramObject);
		}else if(paramObject instanceof Integer){
			params.put(ServiceConstants.COMPANYID, (Integer)paramObject);
		}else if(paramObject instanceof UpdateTimeRange){
			Map<String,String> map = new HashMap<>();
			try {
				map = ResultConvertor.convertToMapWithBeanUtil((UpdateTimeRange)paramObject);
			} catch (Exception e) {
				logger.error("convert UpdateTimeRange to map error: "+e.getMessage());
			}
			params.putAll(map);
		}else if(paramObject instanceof Map){
			Map<String,Object> map = (Map<String, Object>) paramObject;
			params.putAll(map);
		}
		
		return params;
	}

	public List queryForList(String sql, Map paramMap) {
		if(StringUtils.isBlank(sql)){
			return new ArrayList<>();
		}
		generateParamMapWithSql(paramMap,sql);
		
		String selectBy = generateSelectBy(sql);
		
		return getSqlMapClientTemplate().queryForList(selectBy, paramMap);
	}

	@Override
	public Map queryForObject(String sql) {
		return (Map) getSqlMapClientTemplate().queryForObject(SELECT_BY_SQL_STR, sql);
	}

	@Override
	public Map queryForObject(String sql, Object paramObject) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.putAll(generateParamMap(paramObject));
		return queryForObject(sql,params);
	}

	public Map queryForObject(String sql, Map paramMap) {
		generateParamMapWithSql(paramMap,sql);
		
		String selectBy = generateSelectBy(sql);

		return (Map) getSqlMapClientTemplate().queryForObject(selectBy, paramMap);
	}
	
	private String generateSelectBy(String sql) {
		if(null != sql && !sql.toLowerCase().contains(ServiceConstants.WHERE)){
			return SELECT_BY_SQL_STR_WITH_PARAMS;
		}
		return SELECT_BY_SQL_STR_WITH_PARAMS_AND_WHERE;
	}

	private void generateParamMapWithSql(Map paramMap, String sql){
		Matcher n = supplement.matcher(sql);
		Set<String> joinTableNameSet = new HashSet<>();
		/*if(n.find()){
			String tableAliasName = n.group(1);
			sql = sql.toLowerCase().replace(ServiceConstants.WHERE, ServiceConstants.WHERE + " " + tableAliasName + "."+ServiceConstants.COMPANY_ID +"="+paramMap.get(ServiceConstants.COMPANYID) +" and ");
			paramMap.remove(ServiceConstants.COMPANYID);
		}*/
		//获取所有join的tableName
		while(n.find()){
			joinTableNameSet.add(n.group(1));
		}
		if(joinTableNameSet.size() > 0){
			StringBuilder sb = new StringBuilder();
			for(String tableName : joinTableNameSet){
				sb.append(tableName);
				sb.append(ServiceConstants.POINT);
				sb.append(ServiceConstants.COMPANY_ID);
				sb.append(ServiceConstants.EQUAL);
				sb.append(paramMap.get(ServiceConstants.COMPANYID));
				sb.append(ServiceConstants.AND);
			}
			sql = sql.toLowerCase().replace(ServiceConstants.WHERE,ServiceConstants.WHERE + sb);
			/*sql = sql.replace(ServiceConstants.WHERE,ServiceConstants.WHERE + sb);
			sql = sql.replace(ServiceConstants.WHERE.toUpperCase(),ServiceConstants.WHERE.toUpperCase() + sb);*/

			paramMap.remove(ServiceConstants.COMPANYID);
		}
		paramMap.put("sql", sql);
	}

}
