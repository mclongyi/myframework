package com.odianyun.search.whale.data.suggest.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.ibatis.sqlmap.client.SqlMapExecutor;
import com.odianyun.search.whale.data.common.ResultConvertor;
import com.odianyun.search.whale.data.common.ServiceConstants;
import com.odianyun.search.whale.data.model.Area;
import com.odianyun.search.whale.data.model.suggest.WordWithCompany;
import com.odianyun.search.whale.data.saas.model.DBType;
import com.odianyun.search.whale.data.saas.service.BaseDaoService;
import com.odianyun.search.whale.data.saas.service.CompanySqlService;
import com.odianyun.search.whale.data.suggest.dao.ConCurrenceDao;

public class ConCurrenceDaoImpl extends SqlMapClientDaoSupport implements ConCurrenceDao {
//	@Autowired
//	BaseDaoService baseDaoService;
	
	@Override
	public List<WordWithCompany> getWordsWithPage(int companyId, String tableName, String column, long maxId, int pageSize) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> params = new HashMap<>();

		params.put("tableName", tableName);
		params.put("keyword", column);
		params.put("maxId", maxId);
		params.put("pageSize", pageSize);
		params.put(ServiceConstants.COMPANYID, companyId);
		if(tableName.equals("merchant_product")){
			params.put("management_state",1);
		}
		return getSqlMapClientTemplate().queryForList("getWordsWithPage", params);
		
		/*String sql = generateSql(tableName,column);
		Map<String,Object> params = new HashMap<>();
		params.put(ServiceConstants.COMPANYID, companyId);
		params.put(ServiceConstants.MAX_ID, maxId);
        params.put(ServiceConstants.PAGE_SIZE, pageSize);
		List<Map> list = baseDaoService.getBaseDao(companyId, dbType).queryForList(sql,params);
		List<WordWithCompany> result = new ArrayList<WordWithCompany>();
		if(CollectionUtils.isNotEmpty(list)){
			result = ResultConvertor.convertFromMap(list, WordWithCompany.class);
		}
		return result;*/
	}

	

	private String generateSql(String tableName, String column) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, ");
		sql.append(column);
		sql.append(" keyword ,company_id from ");
		sql.append(tableName);
		sql.append(" where is_deleted = 0 and ");
		sql.append(column);
		sql.append(" is not null");

		return sql.toString();
	}



/*	@Override
	public List<WordWithCompany> getConCurrenceWordsWithPage(int pageNo, int pageSize) throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> params = new HashMap<>();
		int startIndex = (pageNo - 1) * pageSize;
        params.put("startIndex", startIndex);
        params.put("pageSize", pageSize);

		return getSqlMapClientTemplate().queryForList("getConCurrenceWordsWithPage", params);	
	}*/

	

}
