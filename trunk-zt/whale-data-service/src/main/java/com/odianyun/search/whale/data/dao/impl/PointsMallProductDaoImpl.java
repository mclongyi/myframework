package com.odianyun.search.whale.data.dao.impl;

import com.odianyun.search.whale.data.common.ServiceConstants;
import com.odianyun.search.whale.data.dao.PointsMallProductDao;
import com.odianyun.search.whale.data.model.*;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PointsMallProductDaoImpl extends SqlMapClientDaoSupport implements PointsMallProductDao{

	@Override
	public List<PointsMallProduct> getPointsMallProductsWithPage(long maxId, int pageSize, Integer companyId) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(ServiceConstants.MAX_ID, maxId);
		params.put(ServiceConstants.PAGE_SIZE, pageSize);
		params.put(ServiceConstants.COMPANYID, companyId);

		return getSqlMapClientTemplate().queryForList("getPointsMallProductsWithPage", params);

	}

	@Override
	public List<PointsMallProduct> getPointsMallProductsByRefId(List<Long> mpIdList,Integer refType,int companyId) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("refIdList", mpIdList);
		params.put("refType", refType);
		params.put("companyId", companyId);

		return getSqlMapClientTemplate().queryForList("getPointsMallProductsByRefId", params);

	}

	@Override
	public List<Long> getMpIdListByPointsMallProductId(List<Long> ids, int companyId) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idList", ids);
		params.put("companyId", companyId);

		return getSqlMapClientTemplate().queryForList("getMpIdListByPointsMallProductId", params);

	}

	@Override
	public List<PointsMallProductPrice> getPointsMallProductPrice(List<Long> pointProductIdList, List<Long> pointsRuleIdList, List<Long> mpIdList, int companyId) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pointProductIdList", pointProductIdList);
		params.put("pointsRuleIdList", pointsRuleIdList);
		params.put("mpIdList", mpIdList);
		params.put("companyId", companyId);
		return getSqlMapClientTemplate().queryForList("getPointsMallProductPrice", params);

	}

}
