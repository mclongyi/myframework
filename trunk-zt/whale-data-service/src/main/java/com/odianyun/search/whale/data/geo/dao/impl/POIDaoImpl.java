package com.odianyun.search.whale.data.geo.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.odianyun.search.whale.data.model.geo.MerchantFlag;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.odianyun.search.whale.data.common.ResultConvertor;
import com.odianyun.search.whale.data.common.ServiceConstants;
import com.odianyun.search.whale.data.geo.dao.POIDao;
import com.odianyun.search.whale.data.model.Area;
import com.odianyun.search.whale.data.model.geo.DeliveryArea;
import com.odianyun.search.whale.data.model.geo.POI;
import com.odianyun.search.whale.data.saas.model.DBType;
import com.odianyun.search.whale.data.saas.service.BaseDaoService;
import com.odianyun.search.whale.data.saas.service.CompanySqlService;

public class POIDaoImpl extends SqlMapClientDaoSupport implements POIDao {

	/*@Autowired
	BaseDaoService baseDaoService;
	
	@Autowired
	CompanySqlService companySqlService;*/
	
	@SuppressWarnings("unchecked")
	@Override
	public List<POI> queryAllPOIs(int companyId) throws Exception {
		return getSqlMapClientTemplate().queryForList("queryAllPOIs",companyId);
		/*String sql = companySqlService.getSql(companyId, "queryAllPOIs");
		Map<String,Object> params = new HashMap<>();
		params.put(ServiceConstants.COMPANYID, companyId);
		List<Map> list = baseDaoService.getBaseDao(companyId, DBType.merchant).queryForList(sql,params);
		List<POI> result = new ArrayList<POI>();
		if(CollectionUtils.isNotEmpty(list)){
			result = ResultConvertor.convertFromMap(list, POI.class);
		}
		return result;*/
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<POI> getPOIsByIds(List<Long> merchantIds,int companyId) throws Exception {
		Map<String,Object> params = new HashMap<>();
		params.put(ServiceConstants.IDS, merchantIds);
		params.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("getPOIsByIds", params);
		/*String sql = companySqlService.getSql(companyId, "getPOIsByIds");
		Map<String,Object> params = new HashMap<>();
		params.put(ServiceConstants.IDS, merchantIds);
		params.put(ServiceConstants.COMPANYID, companyId);
		List<Map> list = baseDaoService.getBaseDao(companyId, DBType.merchant).queryForList(sql,params);
		List<POI> result = new ArrayList<POI>();
		if(CollectionUtils.isNotEmpty(list)){
			result = ResultConvertor.convertFromMap(list, POI.class);
		}
		return result;*/
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<POI> getPOIsWithPage(int pageNo, int pageSize,int companyId)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
        int startIndex = (pageNo - 1) * pageSize;
        params.put("startIndex", startIndex);
        params.put("pageSize", pageSize);
		params.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("getPOIsWithPage",params);
		/*String sql = companySqlService.getSql(companyId, "getPOIsWithPage");
		Map<String,Object> params = new HashMap<>();
		params.put(ServiceConstants.MAX_ID, pageNo);
        params.put(ServiceConstants.PAGE_SIZE, pageSize);
		params.put(ServiceConstants.COMPANYID, companyId);
		List<Map> list = baseDaoService.getBaseDao(companyId, DBType.merchant).queryForList(sql,params);
		List<POI> result = new ArrayList<POI>();
		if(CollectionUtils.isNotEmpty(list)){
			result = ResultConvertor.convertFromMap(list, POI.class);
		}
		return result;*/
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DeliveryArea> queryDeliveryAreasWithPage(int pageNo,
			int pageSize,int companyId) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
        int startIndex = (pageNo - 1) * pageSize;
        params.put("startIndex", startIndex);
        params.put("pageSize", pageSize);
		params.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("queryDeliveryAreasWithPage",params);
		/*String sql = companySqlService.getSql(companyId, "queryDeliveryAreasWithPage");
		Map<String,Object> params = new HashMap<>();
		int startIndex = (pageNo - 1) * pageSize;
        params.put(ServiceConstants.START_INDEX, startIndex);
        params.put(ServiceConstants.PAGE_SIZE, pageSize);
		params.put(ServiceConstants.COMPANYID, companyId);
		List<Map> list = baseDaoService.getBaseDao(companyId, DBType.merchant).queryForList(sql,params);
		List<DeliveryArea> result = new ArrayList<DeliveryArea>();
		if(CollectionUtils.isNotEmpty(list)){
			result = ResultConvertor.convertFromMap(list, DeliveryArea.class);
		}
		return result;*/
	}

	@Override
	public List<MerchantFlag> queryAllFlags(int companyId) throws Exception {
		return getSqlMapClientTemplate().queryForList("queryAllFlags",companyId);
	}

	@Override
	public List<DeliveryArea> queryDeliveryAreasByIds(List<Long> merchantIds, int companyId) throws Exception {
		Map<String,Object> params = new HashMap<>();
		params.put(ServiceConstants.IDS, merchantIds);
		params.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("queryDeliveryAreasByIds", params);
	}

	@Override
	public List<MerchantFlag> queryFlagsByIds(List<Long> merchantIds, int companyId) throws Exception {
		Map<String,Object> params = new HashMap<>();
		params.put(ServiceConstants.IDS, merchantIds);
		params.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("queryFlagsByIds", params);
	}


}
