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
import com.odianyun.search.whale.data.dao.ProductDao;
import com.odianyun.search.whale.data.model.MerchantProdMerchantCateTreeNode;
import com.odianyun.search.whale.data.model.Product;
import com.odianyun.search.whale.data.model.ProductAttributeValue;
import com.odianyun.search.whale.data.saas.model.DBType;
import com.odianyun.search.whale.data.saas.service.BaseDaoService;
import com.odianyun.search.whale.data.saas.service.CompanySqlService;

public class ProductDaoImpl extends SqlMapClientDaoSupport implements ProductDao{

	@Override
	public List<Product> getProducts(List<Long> productIds,int companyId) throws Exception {
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.IDS, productIds);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("queryProducts",paramMap);
	}

	@Override
	public List<MerchantProdMerchantCateTreeNode> getMerchantCateTreeNodeIds(List<Long> merchantProductIds, int companyId) throws Exception{
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.IDS, merchantProductIds);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("getMerchantCateTreeNodeIds", paramMap);

	}

	@Override
	public List<Long> getProductsByCategoryTreeNodeIds(List<Long> categoryTreeNodeIds, int companyId) throws Exception {
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.IDS, categoryTreeNodeIds);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("getProductsByCategoryTreeNodeIds", paramMap);
	}

	@Override
	public List<Long> getProductsByBrandIds(List<Long> brandIds, int companyId) {
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.IDS, brandIds);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("getProductsByBrandIds", paramMap);
	}

	@Override
	public List<Long> getRightCategoryTreeNodeIds(List<Long> categoryTreeNodeIds, int companyId) {
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.IDS, categoryTreeNodeIds);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("getRightCategoryTreeNodeIds", paramMap);
	}

	@Override
	public List<Long> getBrandsByProductIds(List<Long> productIdList, int companyId) throws Exception {
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.IDS, productIdList);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("getBrandsByProductIds",paramMap);
	}

	@Override
	public List<Long> getLeftCategoryIdsByProductIds(List<Long> productIdList, int companyId) throws Exception {
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.IDS, productIdList);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("getLeftCategoryTreeNodeIdsByProductIds",paramMap);
	}

	@Override
	public List<Long> getLeftCategoryIdsByProductIds2(List<Long> productIdList, int companyId)
			throws Exception {
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.IDS, productIdList);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("getLeftCategoryTreeNodeIdsByProductIds2",paramMap);
	}

}
