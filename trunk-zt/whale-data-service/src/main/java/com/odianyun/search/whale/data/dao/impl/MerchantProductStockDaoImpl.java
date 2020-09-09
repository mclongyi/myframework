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
import com.odianyun.search.whale.data.dao.MerchantProductStockDao;
import com.odianyun.search.whale.data.model.MerchantProductPrice;
import com.odianyun.search.whale.data.model.MerchantProductStock;
import com.odianyun.search.whale.data.saas.model.DBType;
import com.odianyun.search.whale.data.saas.service.BaseDaoService;
import com.odianyun.search.whale.data.saas.service.CompanySqlService;

public class MerchantProductStockDaoImpl extends SqlMapClientDaoSupport implements MerchantProductStockDao {

	/*@Autowired
	BaseDaoService baseDaoService;
	
	@Autowired
	CompanySqlService companySqlService;*/
	
	/*@Override
	public List<MerchantProductStock> queryAllMerchantProductStocks(int companyId) throws Exception{
		String sql = companySqlService.getSql(companyId, "queryAllMerchantProductStocks");
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		List<Map> list = baseDaoService.getBaseDao(companyId, DBType.stock).queryForList(sql,paramMap);
		List<MerchantProductStock> result = new ArrayList<MerchantProductStock>();
		if(CollectionUtils.isNotEmpty(list)){
			result = ResultConvertor.convertFromMap(list, MerchantProductStock.class);
		}
		return result;
		return getSqlMapClientTemplate().queryForList("queryAllMerchantProductStocks",companyId);
	}*/

	@Override
	public List<MerchantProductStock> getMerchantProductStocks(
			List<Long> merchantProductIds, int companyId) throws Exception{
		/*String sql = companySqlService.getSql(companyId, "getMerchantProductStocksByIds");
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		paramMap.put(ServiceConstants.IDS, merchantProductIds);
		List<Map> list = baseDaoService.getBaseDao(companyId, DBType.stock).queryForList(sql,paramMap);
		List<MerchantProductStock> result = new ArrayList<MerchantProductStock>();
		if(CollectionUtils.isNotEmpty(list)){
			result = ResultConvertor.convertFromMap(list, MerchantProductStock.class);
		}
		return result;*/
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.IDS, merchantProductIds);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("getMerchantProductStocksByIds",paramMap);
	}

}
