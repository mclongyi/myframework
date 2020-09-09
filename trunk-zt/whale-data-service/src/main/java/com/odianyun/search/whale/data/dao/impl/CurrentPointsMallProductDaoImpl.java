package com.odianyun.search.whale.data.dao.impl;

import com.odianyun.search.whale.data.common.ServiceConstants;
import com.odianyun.search.whale.data.dao.CurrentPointsMallProductDao;
import com.odianyun.search.whale.data.dao.PointsMallProductDao;
import com.odianyun.search.whale.data.model.PointsMallProduct;
import com.odianyun.search.whale.data.model.PointsMallProductPrice;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurrentPointsMallProductDaoImpl extends SqlMapClientDaoSupport implements CurrentPointsMallProductDao {

	@Override
	public List<PointsMallProduct> getPointsMallProductsByRefId(List<Long> mpIdList,int companyId) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("refIdList", mpIdList);
		params.put("companyId", companyId);

		return getSqlMapClientTemplate().queryForList("getPointsMallProductsByRefIdCurrent", params);

	}
}
