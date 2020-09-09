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
import com.odianyun.search.whale.data.dao.AttributeValueDao;
import com.odianyun.search.whale.data.model.Area;
import com.odianyun.search.whale.data.model.AttributeValue;
import com.odianyun.search.whale.data.saas.model.DBType;
import com.odianyun.search.whale.data.saas.service.BaseDaoService;
import com.odianyun.search.whale.data.saas.service.CompanySqlService;

public class AttributeValueDaoImpl extends SqlMapClientDaoSupport implements AttributeValueDao{

	/*@Autowired
	BaseDaoService baseDaoService;
	
	@Autowired
	CompanySqlService companySqlService;*/
	
	@Override
	public List<AttributeValue> queryAllAttributeValue(int companyId) throws Exception{
		/*String sql = companySqlService.getSql(companyId, "queryAllAttributeValue");
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		List<Map> list = baseDaoService.getBaseDao(companyId, DBType.product).queryForList(sql,paramMap);
		List<AttributeValue> result = new ArrayList<AttributeValue>();
		if(CollectionUtils.isNotEmpty(list)){
			result = ResultConvertor.convertFromMap(list, AttributeValue.class);
		}
		return result;*/

		return getSqlMapClientTemplate().queryForList("queryAllAttributeValue",companyId);
	}

}
