package com.odianyun.search.whale.data.dao.impl;

import java.util.HashMap;
import java.util.List;

import com.odianyun.search.whale.data.model.*;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.odianyun.search.whale.data.common.ServiceConstants;
import com.odianyun.search.whale.data.dao.CategoryDao;

public class CategoryDaoImpl extends SqlMapClientDaoSupport implements CategoryDao{

	/*@Autowired
	BaseDaoService baseDaoService;
	
	@Autowired
	CompanySqlService companySqlService;*/
	
	@Override
	public List<Category> queryAllCategory(int companyId) throws Exception {
		/*String sql = companySqlService.getSql(companyId, "queryAllCategory");
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		List<Map> list = baseDaoService.getBaseDao(companyId, DBType.product).queryForList(sql,paramMap);
		List<Category> result = new ArrayList<Category>();
		if(CollectionUtils.isNotEmpty(list)){
			result = ResultConvertor.convertFromMap(list, Category.class);
		}
		return result;*/

		return getSqlMapClientTemplate().queryForList("queryAllCategory",companyId);
	}

	@Override
	public List<CategoryTreeNode> queryAllCategoryTree(int companyId) throws Exception {
		/*String sql = companySqlService.getSql(companyId, "queryAllCategoryTreeNode");
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		List<Map> list = baseDaoService.getBaseDao(companyId, DBType.product).queryForList(sql,paramMap);
		List<CategoryTreeNode> result = new ArrayList<CategoryTreeNode>();
		if(CollectionUtils.isNotEmpty(list)){
			result = ResultConvertor.convertFromMap(list, CategoryTreeNode.class);
		}
		return result;*/
		return getSqlMapClientTemplate().queryForList("queryAllCategoryTreeNode",companyId);
	}

	@Override
	public List<Category> getCategorys(List<Long> categoryIds, int companyId) throws Exception {
		/*String sql = companySqlService.getSql(companyId, "queryCategorys");
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		paramMap.put(ServiceConstants.IDS, categoryIds);
		List<Map> list = baseDaoService.getBaseDao(companyId, DBType.product).queryForList(sql,paramMap);
		List<Category> result = new ArrayList<Category>();
		if(CollectionUtils.isNotEmpty(list)){
			result = ResultConvertor.convertFromMap(list, Category.class);
		}
		return result;*/
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.IDS, categoryIds);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("queryCategorys",paramMap);
	}

	/*@Override
	public CategoryTreeNode getCategoryTreeNode(Long category_tree_node_id, int companyId)
			throws Exception {
		String sql = companySqlService.getSql(companyId, "queryCategoryTreeNode");
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		paramMap.put(ServiceConstants.ID, category_tree_node_id);
		Map map = baseDaoService.getBaseDao(companyId, DBType.product).queryForObject(sql,paramMap);
		CategoryTreeNode categoryTreeNode = null;
		if(null != map){
			categoryTreeNode = ResultConvertor.convertFromMap(map, CategoryTreeNode.class);
		}
		return categoryTreeNode;
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.ID, category_tree_node_id);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		return (CategoryTreeNode) getSqlMapClientTemplate().queryForObject("queryCategoryTreeNode", paramMap);
	}*/

	@Override
	public Category getCategory(Long categoryId, int companyId) throws Exception {
		/*String sql = companySqlService.getSql(companyId, "queryCategory");
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		paramMap.put(ServiceConstants.ID, categoryId);
		Map map = baseDaoService.getBaseDao(companyId, DBType.product).queryForObject(sql,paramMap);
		Category category = null;
		if(null != map){
			category = ResultConvertor.convertFromMap(map, Category.class);
		}
		return category;*/
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.ID, categoryId);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		return (Category) getSqlMapClientTemplate().queryForObject("queryCategory",paramMap);
	}

	@Override
	public List<CategoryTreeNodeRelation> queryAllCategoryRelation(int companyId)
			throws Exception {
		/*String sql = companySqlService.getSql(companyId, "queryAllCategoryTreeNodeRelation");
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		List<Map> list = baseDaoService.getBaseDao(companyId, DBType.product).queryForList(sql,paramMap);
		List<CategoryTreeNodeRelation> result = new ArrayList<CategoryTreeNodeRelation>();
		if(CollectionUtils.isNotEmpty(list)){
			result = ResultConvertor.convertFromMap(list, CategoryTreeNodeRelation.class);
		}
		return result;*/
		return getSqlMapClientTemplate().queryForList("queryAllCategoryTreeNodeRelation",companyId);
	}
	
	/*@Override
	public List<CategoryTreeNodeRelation> queryCategoryTreeNodeRelation(
			Long right_tree_node_id, int companyId) throws Exception {
		String sql = companySqlService.getSql(companyId, "queryCategoryTreeNodeRelation");
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		paramMap.put(ServiceConstants.ID, right_tree_node_id);
		List<Map> list = baseDaoService.getBaseDao(companyId, DBType.product).queryForList(sql,paramMap);
		List<CategoryTreeNodeRelation> result = new ArrayList<CategoryTreeNodeRelation>();
		if(CollectionUtils.isNotEmpty(list)){
			result = ResultConvertor.convertFromMap(list, CategoryTreeNodeRelation.class);

		}
		return result;
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.ID, right_tree_node_id);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("queryCategoryTreeNodeRelation",paramMap);
	}*/


	/*@Override
	public List<Category> queryCategoriesByLeftTreeNodeIds(List<Long> ids, int companyId) throws Exception {
		String sql = companySqlService.getSql(companyId, "queryCategoriesByLeftTreeNodeIds");
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		paramMap.put(ServiceConstants.IDS, ids);
		List<Map> list = baseDaoService.getBaseDao(companyId, DBType.product).queryForList(sql,paramMap);
		List<Category> result = new ArrayList<Category>();
		if(CollectionUtils.isNotEmpty(list)){
			result = ResultConvertor.convertFromMap(list, Category.class);
		}
		return result;
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.IDS, ids);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("queryCategoriesByLeftTreeNodeIds",paramMap);
	}*/

	/*@Override
	public List<CategoryTreeNode> queryCategoryTreesByLeftTreeNodeIds(List<Long> ids, int companyId) throws Exception {

		String sql = companySqlService.getSql(companyId, "queryCategoryTreesByLeftTreeNodeIds");
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		paramMap.put(ServiceConstants.IDS, ids);
		List<Map> list = baseDaoService.getBaseDao(companyId, DBType.product).queryForList(sql,paramMap);
		List<CategoryTreeNode> result = new ArrayList<CategoryTreeNode>();
		if(CollectionUtils.isNotEmpty(list)){
			result = ResultConvertor.convertFromMap(list, CategoryTreeNode.class);
		}
		return result;
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.IDS, ids);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("queryCategoryTreesByLeftTreeNodeIds",paramMap);
	}*/

	@Override
	public List<CategoryTreeNodeRelation> queryCategoryRelationsByLeftCetgoryIds(List<Long> ids, int companyId)
			throws Exception {
		/*String sql = companySqlService.getSql(companyId, "queryCategoryTreeNodeRelationsByLeftTreeNodeIds");
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		paramMap.put(ServiceConstants.IDS, ids);
		List<Map> list = baseDaoService.getBaseDao(companyId, DBType.product).queryForList(sql,paramMap);
		List<CategoryTreeNodeRelation> result = new ArrayList<CategoryTreeNodeRelation>();
		if(CollectionUtils.isNotEmpty(list)){
			result = ResultConvertor.convertFromMap(list, CategoryTreeNodeRelation.class);
		}
		return result;*/
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.IDS, ids);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("queryCategoryTreeNodeRelationsByLeftTreeNodeIds",paramMap);
	}

	@Override
	public List<ProductCategoryRelation> queryProductCategoryRelation(int companyId) throws Exception {
		return getSqlMapClientTemplate().queryForList("queryProductCategoryRelation", companyId);
	}

	@Override
	public List<MPCategoryRelation> queryMerchantProductCategory(List<Long> ids,int companyId) throws Exception {
		HashMap<String,Object> param = new HashMap<String,Object>();
		param.put(ServiceConstants.IDS,ids);
		param.put(ServiceConstants.COMPANYID,companyId);
		return getSqlMapClientTemplate().queryForList("queryMerchantProductCategory",param);
	}

	@Override
	public List<MPCategoryRelation> queryMerchantProductCategoryByCateIds(List<Long> ids, int companyId) throws Exception {
		HashMap<String,Object> param = new HashMap<String,Object>();
		param.put(ServiceConstants.IDS,ids);
		param.put(ServiceConstants.COMPANYID,companyId);
		return getSqlMapClientTemplate().queryForList("queryMerchantProductCategoryByCateIds",param);
	}

	@Override
	public List<MPCategoryRelation> queryAllMerchantProductCategory(int companyId) throws Exception {
		return getSqlMapClientTemplate().queryForList("queryAllMerchantProductCategory",companyId);
	}

	@Override
	public List<Category> queryCategoriesByLeftCategoryIds(List<Long> ids, int companyId) throws Exception {
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put(ServiceConstants.IDS, ids);
		paramMap.put(ServiceConstants.COMPANYID, companyId);
		return getSqlMapClientTemplate().queryForList("queryCategoriesByLeftCategoryIds",paramMap);

	}

}
