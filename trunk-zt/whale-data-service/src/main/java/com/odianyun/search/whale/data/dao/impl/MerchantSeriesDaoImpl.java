package com.odianyun.search.whale.data.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.odianyun.search.whale.data.common.ServiceConstants;
import com.odianyun.search.whale.data.dao.MerchantSeriesDao;
import com.odianyun.search.whale.data.model.MerchantSeries;
import com.odianyun.search.whale.data.model.MerchantSeriesAttribute;

public class MerchantSeriesDaoImpl extends SqlMapClientDaoSupport implements MerchantSeriesDao {



	@SuppressWarnings("unchecked")
	@Override
	public List<MerchantSeries> querySeriesList(List<Long> merchantSeriesIdList, int companyId) throws Exception {

		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.IDS, merchantSeriesIdList);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("queryMerchantSeriesList",paramMap);
	}

	@Override
	public List<MerchantSeriesAttribute> querySeriesAttributeValue(
			List<Long> merchantSeriesIdList, int companyId) throws Exception {

		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.IDS, merchantSeriesIdList);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("querySeriesAttrValueList",paramMap);
	}

	@Override
	public Map<Long, Long> queryMpId2SeriesId(List<Long> merchantSeriesIdList, int companyId) throws Exception {
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.IDS, merchantSeriesIdList);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForMap("queryMpId2SeriesId",paramMap,"merchant_product_id","seriesId");
	}

	@Override
	public Map<Long, Long> queryMpId2SeriesIdAll(List<Long> merchantSeriesIdList, int companyId) throws Exception {
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.IDS, merchantSeriesIdList);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForMap("queryMpId2SeriesIdAll",paramMap,"merchant_product_id","seriesId");
	}
	

}
