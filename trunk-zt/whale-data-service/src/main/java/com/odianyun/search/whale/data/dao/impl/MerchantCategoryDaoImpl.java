package com.odianyun.search.whale.data.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.odianyun.search.whale.data.model.MerchantCateCategoryTreeNodeRel;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.odianyun.search.whale.data.common.ResultConvertor;
import com.odianyun.search.whale.data.common.ServiceConstants;
import com.odianyun.search.whale.data.dao.MerchantCategoryDao;
import com.odianyun.search.whale.data.model.Category;
import com.odianyun.search.whale.data.model.MerchantCateTreeNode;
import com.odianyun.search.whale.data.saas.model.DBType;
import com.odianyun.search.whale.data.saas.service.BaseDaoService;
import com.odianyun.search.whale.data.saas.service.CompanySqlService;

public class MerchantCategoryDaoImpl extends SqlMapClientDaoSupport implements MerchantCategoryDao{

	/*@Autowired
	BaseDaoService baseDaoService;
	
	@Autowired
	CompanySqlService companySqlService;*/
	
	@SuppressWarnings("unchecked")
	@Override
	public List<MerchantCateTreeNode> queryAllMerchantCateTreeNode(int companyId)
			throws Exception {
		/*String sql = companySqlService.getSql(companyId, "queryAllMerchantCateTreeNodes");
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		List<Map> list = baseDaoService.getBaseDao(companyId, DBType.product).queryForList(sql,paramMap);
		List<MerchantCateTreeNode> result = new ArrayList<MerchantCateTreeNode>();
		if(CollectionUtils.isNotEmpty(list)){
			result = ResultConvertor.convertFromMap(list, MerchantCateTreeNode.class);
		}
		return result;*/
		return getSqlMapClientTemplate().queryForList("queryAllMerchantCateTreeNodes",companyId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MerchantCateTreeNode> getMerchantCateTreeNodes(
			List<Long> merchant_cate_tree_node_ids, int companyId) throws Exception {
		/*String sql = companySqlService.getSql(companyId, "getMerchantCateTreeNodes");
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		paramMap.put(ServiceConstants.IDS, merchant_cate_tree_node_ids);
		List<Map> list = baseDaoService.getBaseDao(companyId, DBType.product).queryForList(sql,paramMap);
		List<MerchantCateTreeNode> result = new ArrayList<MerchantCateTreeNode>();
		if(CollectionUtils.isNotEmpty(list)){
			result = ResultConvertor.convertFromMap(list, MerchantCateTreeNode.class);
		}
		return result;*/
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.IDS, merchant_cate_tree_node_ids);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("getMerchantCateTreeNodes",paramMap);
	}

	@Override
	public MerchantCateTreeNode getMerchantCateTreeNode(
			long merchant_cat_tree_node_id, int companyId) throws Exception {
		/*String sql = companySqlService.getSql(companyId, "getMerchantCateTreeNodeById");
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		paramMap.put(ServiceConstants.ID, merchant_cat_tree_node_id);
		Map map = baseDaoService.getBaseDao(companyId, DBType.product).queryForObject(sql,paramMap);
		MerchantCateTreeNode result = new MerchantCateTreeNode();
		if(null != map){
			result = ResultConvertor.convertFromMap(map, MerchantCateTreeNode.class);
		}
		return result;*/
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.ID, merchant_cat_tree_node_id);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		return (MerchantCateTreeNode) getSqlMapClientTemplate().queryForObject(
				"getMerchantCateTreeNodeById",paramMap);
	}

	@Override
	public List<MerchantCateCategoryTreeNodeRel> queryAllMerchantCateCateTreeNodeRel(int companyId) throws Exception {
		return getSqlMapClientTemplate().queryForList("queryAllMerchantCateCateTreeNodeRel",companyId);
	}


}
