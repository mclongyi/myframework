package com.odianyun.search.whale.data.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.odianyun.search.whale.data.model.geo.MerchantStoreCalendar;
import com.odianyun.search.whale.data.model.geo.MerchantStoreCalendarItem;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.odianyun.search.whale.data.common.ResultConvertor;
import com.odianyun.search.whale.data.common.ServiceConstants;
import com.odianyun.search.whale.data.dao.MerchantDao;
import com.odianyun.search.whale.data.model.Merchant;
import com.odianyun.search.whale.data.model.MerchantBelongArea;
import com.odianyun.search.whale.data.model.MerchantCateTreeNode;
import com.odianyun.search.whale.data.model.Shop;
import com.odianyun.search.whale.data.saas.model.DBType;
import com.odianyun.search.whale.data.saas.service.BaseDaoService;
import com.odianyun.search.whale.data.saas.service.CompanySqlService;

public class MerchantDaoImpl extends SqlMapClientDaoSupport implements MerchantDao {

	/*@Autowired
	BaseDaoService baseDaoService;
	
	@Autowired
	CompanySqlService companySqlService;*/
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Merchant> queryAllMerchant(int companyId) throws Exception {
		/*String sql = companySqlService.getSql(companyId, "queryAllMerchant");
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		List<Map> list = baseDaoService.getBaseDao(companyId, DBType.merchant).queryForList(sql,paramMap);
		List<Merchant> result = new ArrayList<Merchant>();
		if(CollectionUtils.isNotEmpty(list)){
			result = ResultConvertor.convertFromMap(list, Merchant.class);
		}
		return result;*/
		return getSqlMapClientTemplate().queryForList("queryAllMerchant",companyId);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Merchant> queryMerchantsWithPage(int pageNo, int pageSize, int companyId)
			throws Exception {
		/*Map<String, Object> params = new HashMap<String, Object>();
		int startIndex = (pageNo - 1) * pageSize;
        params.put(ServiceConstants.START_INDEX, startIndex);
        params.put(ServiceConstants.PAGE_SIZE, pageSize);
		params.put(ServiceConstants.COMPANYID, companyId);
        String sql = companySqlService.getSql(companyId, "queryMerchantsWithPage");
		List<Map> list = baseDaoService.getBaseDao(companyId, DBType.merchant).queryForList(sql,params);
		List<Merchant> result = new ArrayList<Merchant>();
		if(CollectionUtils.isNotEmpty(list)){
			result = ResultConvertor.convertFromMap(list, Merchant.class);
		}
		return result;*/
		Map<String, Object> params = new HashMap<String, Object>();
		int startIndex = (pageNo - 1) * pageSize;
        params.put(ServiceConstants.START_INDEX, startIndex);
        params.put(ServiceConstants.PAGE_SIZE, pageSize);
		params.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("queryMerchantsWithPage", params);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Merchant> getMerchants(List<Long> merchantIds, int companyId) throws Exception {
		/*String sql = companySqlService.getSql(companyId, "queryMerchants");
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		paramMap.put("ids", merchantIds);
		List<Map> list = baseDaoService.getBaseDao(companyId, DBType.merchant).queryForList(sql,paramMap);
		List<Merchant> result = new ArrayList<Merchant>();
		if(CollectionUtils.isNotEmpty(list)){
			result = ResultConvertor.convertFromMap(list, Merchant.class);
		}
		return result;*/
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.IDS, merchantIds);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("queryMerchants",paramMap);
	}

	@Override
	public List<Shop> queryAllShops(int companyId) throws Exception {
		/*String sql = companySqlService.getSql(companyId, "queryAllShops");
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		List<Map> list = baseDaoService.getBaseDao(companyId, DBType.merchant).queryForList(sql,paramMap);
		List<Shop> result = new ArrayList<Shop>();
		if(CollectionUtils.isNotEmpty(list)){
			result = ResultConvertor.convertFromMap(list, Shop.class);
		}
		return result;*/
		return getSqlMapClientTemplate().queryForList("queryAllShops",companyId);
	}

	@Override
	public List<Shop> getShopsByMerchantIds(List<Long> merchantIds, int companyId)
			throws Exception {
		/*String sql = companySqlService.getSql(companyId, "getShopsByMerchantIds");
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		paramMap.put(ServiceConstants.IDS, merchantIds);
		List<Map> list = baseDaoService.getBaseDao(companyId, DBType.merchant).queryForList(sql,paramMap);
		List<Shop> result = new ArrayList<Shop>();
		if(CollectionUtils.isNotEmpty(list)){
			result = ResultConvertor.convertFromMap(list, Shop.class);
		}
		return result;*/
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.IDS, merchantIds);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("getShopsByMerchantIds",paramMap);
	}

	@Override
	public List<MerchantBelongArea> queryAllBelongAreas(int companyId) throws Exception {
		/*String sql = companySqlService.getSql(companyId, "queryAllBelongAreas");
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		List<Map> list = baseDaoService.getBaseDao(companyId, DBType.merchant).queryForList(sql,paramMap);
		List<MerchantBelongArea> result = new ArrayList<MerchantBelongArea>();
		if(CollectionUtils.isNotEmpty(list)){
			result = ResultConvertor.convertFromMap(list, MerchantBelongArea.class);
		}
		return result;*/
		return getSqlMapClientTemplate().queryForList("queryAllBelongAreas",companyId);
	}

	@Override
	public List<MerchantBelongArea> getBelongAreasByMerchantIds(
			List<Long> merchantIds, int companyId) throws Exception {
		/*String sql = companySqlService.getSql(companyId, "getBelongAreasByMerchantIds");
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		paramMap.put(ServiceConstants.IDS, merchantIds);
		List<Map> list = baseDaoService.getBaseDao(companyId, DBType.merchant).queryForList(sql,paramMap);
		List<MerchantBelongArea> result = new ArrayList<MerchantBelongArea>();
		if(CollectionUtils.isNotEmpty(list)){
			result = ResultConvertor.convertFromMap(list, MerchantBelongArea.class);
		}
		return result;*/
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.IDS, merchantIds);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("getBelongAreasByMerchantIds",paramMap);
	}

	@Override
	public List<Merchant> getMerchantsByIds(List<Long> merchantIds, int companyId) throws Exception {
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.IDS, merchantIds);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("queryMerchants",paramMap);
	}

	@Override
	public List<MerchantStoreCalendar> queryMerchantStoreCalendars(int weekIndex, int companyId) throws Exception {
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.WEEK_INDEX, weekIndex);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("queryMerchantStoreCalendars",paramMap);

	}

	@Override
	public List<MerchantStoreCalendarItem> queryMerchantStoreCalendarItems(int companyId) throws Exception {
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("queryMerchantStoreCalendarItems",paramMap);

	}

	@Override
	public List<MerchantStoreCalendarItem> queryMerchantStoreCalendarItemsByIds(List<Long> merchantStoreCalendarIds, int companyId) {
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		paramMap.put(ServiceConstants.MERCHANT_STORE_CALENDAR_ID, merchantStoreCalendarIds);
		return getSqlMapClientTemplate().queryForList("queryMerchantStoreCalendarItems",paramMap);

	}

	@Override
	public List<MerchantStoreCalendar> queryMerchantStoreCalendarsByIds(List<Long> merchantIds, int weekIndex, int companyId) throws Exception {
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.WEEK_INDEX, weekIndex);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		paramMap.put(ServiceConstants.MERCHANT_ID, merchantIds);
		return getSqlMapClientTemplate().queryForList("queryMerchantStoreCalendars",paramMap);

	}


}
