package com.odianyun.search.whale.data.dao.impl;

import java.util.HashMap;
import java.util.List;

import com.odianyun.search.whale.data.model.MerchantProdAttValue;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.odianyun.search.whale.data.common.ServiceConstants;
import com.odianyun.search.whale.data.dao.ProductAttributeDao;
import com.odianyun.search.whale.data.model.MerchantProductAttributeValue;
import com.odianyun.search.whale.data.model.ProductAttributeValue;

public class ProductAttributeDaoImpl extends SqlMapClientDaoSupport implements ProductAttributeDao{

	/*@Autowired
	BaseDaoService baseDaoService;
	
	@Autowired
	CompanySqlService companySqlService;*/
	
	/*@Override
	public List<ProductAttributeValue> queryAllProductAttributeValues(int companyId) throws Exception {
		String sql = companySqlService.getSql(companyId, "queryAllProductAttributeValues");
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		List<Map> list = baseDaoService.getBaseDao(companyId, DBType.product).queryForList(sql,paramMap);
		List<ProductAttributeValue> result = new ArrayList<ProductAttributeValue>();
		if(CollectionUtils.isNotEmpty(list)){
			result = ResultConvertor.convertFromMap(list, ProductAttributeValue.class);
		}
		return result;
		return getSqlMapClientTemplate().queryForList("queryAllProductAttributeValues",companyId);
	}*/

	@Override
	public List<ProductAttributeValue> queryProductAttributeValues(
			List<Long> productIds, int companyId) throws Exception {
		/*String sql = companySqlService.getSql(companyId, "queryProductAttributeValues");
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		paramMap.put(ServiceConstants.IDS, productIds);
		List<Map> list = baseDaoService.getBaseDao(companyId, DBType.product).queryForList(sql,paramMap);
		List<ProductAttributeValue> result = new ArrayList<ProductAttributeValue>();
		if(CollectionUtils.isNotEmpty(list)){
			result = ResultConvertor.convertFromMap(list, ProductAttributeValue.class);
		}
		return result;*/
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.IDS, productIds);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("queryProductAttributeValues",paramMap);
	}

	@Override
	public List<MerchantProductAttributeValue> queryMerchantProductAttributeValues(List<Long> merchantProductIds,
			int companyId) throws Exception {
		/*String sql = companySqlService.getSql(companyId, "queryMerchantProductAttributeValues");
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		paramMap.put(ServiceConstants.IDS, merchantProductIds);
		List<Map> list = baseDaoService.getBaseDao(companyId, DBType.product).queryForList(sql,paramMap);
		List<MerchantProductAttributeValue> result = new ArrayList<MerchantProductAttributeValue>();
		if(CollectionUtils.isNotEmpty(list)){
			result = ResultConvertor.convertFromMap(list, MerchantProductAttributeValue.class);
		}
		return result;*/
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.IDS, merchantProductIds);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("queryMerchantProductAttributeValues",paramMap);
	}

	@Override
	public List<MerchantProdAttValue> queryMerchantProdAttValues(List<Long> merchantProductIds, int companyId) throws Exception {
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.IDS, merchantProductIds);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("queryMerchantProdAttValues",paramMap);
	}

}
