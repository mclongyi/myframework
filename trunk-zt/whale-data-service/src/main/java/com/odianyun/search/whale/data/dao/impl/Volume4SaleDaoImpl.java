package com.odianyun.search.whale.data.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.odianyun.search.whale.data.model.MerchantProductSaleOffset;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.odianyun.search.whale.data.common.ResultConvertor;
import com.odianyun.search.whale.data.common.ServiceConstants;
import com.odianyun.search.whale.data.dao.Volume4SaleDao;
import com.odianyun.search.whale.data.model.MerchantProductVolume4Sale;
import com.odianyun.search.whale.data.model.Product;
import com.odianyun.search.whale.data.saas.model.DBType;
import com.odianyun.search.whale.data.saas.service.BaseDaoService;
import com.odianyun.search.whale.data.saas.service.CompanySqlService;

public class Volume4SaleDaoImpl extends SqlMapClientDaoSupport implements Volume4SaleDao {

	/*@Autowired
	BaseDaoService baseDaoService;
	
	@Autowired
	CompanySqlService companySqlService;*/

//	private String VOLUME_SALE_MP_ID = "merchant_prod_id";
	
	@Override
	public List<MerchantProductVolume4Sale> queryVolume4Sale(List<Long> merchantProductIds, int companyId)  throws Exception{
		/*String sql = companySqlService.getSql(companyId, "queryVolume4Sale");
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		paramMap.put(ServiceConstants.IDS, merchantProductIds);
		paramMap.put(ServiceConstants.GROUPBY,VOLUME_SALE_MP_ID);
		List<Map> list = baseDaoService.getBaseDao(companyId, DBType.product).queryForList(sql,paramMap);
		List<MerchantProductVolume4Sale> result = new ArrayList<MerchantProductVolume4Sale>();
		if(CollectionUtils.isNotEmpty(list)){
			result = ResultConvertor.convertFromMap(list, MerchantProductVolume4Sale.class);
		}
		return result;*/
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.IDS, merchantProductIds);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("queryVolume4Sale",paramMap);
	}

	@Override
	public List<MerchantProductSaleOffset> querySaleOffset(List<Long> merchantProductIds, int companyId) throws Exception {
		/*String sql = companySqlService.getSql(companyId, "querySaleOffset");
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		paramMap.put(ServiceConstants.IDS, merchantProductIds);
		List<Map> list = baseDaoService.getBaseDao(companyId, DBType.product).queryForList(sql,paramMap);
		List<MerchantProductSaleOffset> result = new ArrayList<MerchantProductSaleOffset>();
		if(CollectionUtils.isNotEmpty(list)){
			result = ResultConvertor.convertFromMap(list, MerchantProductSaleOffset.class);
		}
		return result;*/
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.IDS, merchantProductIds);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("querySaleOffset",paramMap);
	}


}
