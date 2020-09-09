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
import com.odianyun.search.whale.data.dao.MerchantProductPriceDao;
import com.odianyun.search.whale.data.model.MerchantProduct;
import com.odianyun.search.whale.data.model.MerchantProductPrice;
import com.odianyun.search.whale.data.saas.model.DBType;
import com.odianyun.search.whale.data.saas.service.BaseDaoService;
import com.odianyun.search.whale.data.saas.service.CompanySqlService;

public class MerchantProductPriceDaoImpl extends SqlMapClientDaoSupport implements MerchantProductPriceDao{


	@Override
	public List<MerchantProductPrice> queryMerchantProductPrice(
			List<Long> merchantProductIds, int companyId) throws Exception{
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.IDS, merchantProductIds);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("queryMerchantProductPrice",paramMap);
	}

	@Override
	public List<MerchantProductPrice> queryMerchantProductPromotionPrice(List<Long> merchantProductIds, int companyId) throws Exception {
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.IDS, merchantProductIds);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("queryMerchantProductPromotionPrice",paramMap);
	}


}
