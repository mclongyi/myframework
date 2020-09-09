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
import com.odianyun.search.whale.data.dao.BrandDao;
import com.odianyun.search.whale.data.model.AttributeValue;
import com.odianyun.search.whale.data.model.Brand;
import com.odianyun.search.whale.data.saas.model.DBType;
import com.odianyun.search.whale.data.saas.service.BaseDaoService;
import com.odianyun.search.whale.data.saas.service.CompanySqlService;

public class BrandDaoImpl extends SqlMapClientDaoSupport implements BrandDao{

	/*@Autowired
	BaseDaoService baseDaoService;
	
	@Autowired
	CompanySqlService companySqlService;*/
	
	@Override
	public List<Brand> queryAllBrand(int companyId) throws Exception {
		/*String sql = companySqlService.getSql(companyId, "queryAllBrand");
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		List<Map> list = baseDaoService.getBaseDao(companyId, DBType.product).queryForList(sql,paramMap);
		List<Brand> result = new ArrayList<Brand>();
		if(CollectionUtils.isNotEmpty(list)){
			result = ResultConvertor.convertFromMap(list, Brand.class);
		}
		return result;*/
		return getSqlMapClientTemplate().queryForList("queryAllBrand",companyId);
	}

	@Override
	public List<Brand> getBrands(List<Long> brandIds, int companyId) throws Exception {
		/*String sql = companySqlService.getSql(companyId, "queryBrands");
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		paramMap.put(ServiceConstants.IDS, brandIds);
		List<Map> list = baseDaoService.getBaseDao(companyId, DBType.product).queryForList(sql,paramMap);
		List<Brand> result = new ArrayList<Brand>();
		if(CollectionUtils.isNotEmpty(list)){
			result = ResultConvertor.convertFromMap(list, Brand.class);
		}
		return result;*/
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.IDS, brandIds);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("queryBrands",paramMap);
	}

}
