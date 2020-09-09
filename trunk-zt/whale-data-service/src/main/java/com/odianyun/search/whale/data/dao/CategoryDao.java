package com.odianyun.search.whale.data.dao;

import java.util.List;

import com.odianyun.search.whale.data.model.*;

public interface CategoryDao {

	/**
	 * 读取所有的类目数据,适用于全量索引
	 * @return
	 * @throws Exception
	 */
	public List<Category> queryAllCategory(int companyId) throws Exception;
	
    /**
     * 读取所有的类目树节点数据,适用于全量索引
     * @return
     * @throws Exception
     */
	public List<CategoryTreeNode> queryAllCategoryTree(int companyId) throws Exception;
	
	/**
	 * 根据类目ID列表获取相关的类目数据，适用于实时索引
	 * @param categoryIds
	 * @return
	 * @throws Exception
	 */
	public List<Category> getCategorys(List<Long> categoryIds, int companyId) throws Exception;
	
	/**
	 * 根据类目ID获取相关的类目数据，适用于实时索引
	 * @param categoryId
	 * @return
	 * @throws Exception
	 */
	public Category getCategory(Long categoryId, int companyId) throws Exception;
	
	/**
	 * 根据类目树节点ID读取节点数据，适用于实时索引
	 * @param category_tree_node_id
	 * @return
	 * @throws Exception
	 */
//	public CategoryTreeNode getCategoryTreeNode(Long category_tree_node_id, int companyId) throws Exception;
	
	public List<CategoryTreeNodeRelation> queryAllCategoryRelation(int companyId) throws Exception;
	
//	public List<CategoryTreeNodeRelation> queryCategoryTreeNodeRelation(Long right_tree_node_id, int companyId) throws Exception;
	
	/**
	 * 根据updateTimeRange获取数据，适用于实时索引
	 * @param updateTimeRange
	 * @return
	 * @throws Exception
	 */
//	public List<CategoryTreeNodeRelation> queryCategoryTreeNodeRelationByUpdateTime(UpdateTimeRange updateTimeRange, int companyId) throws Exception;

//	public List<Category> queryCategoriesByLeftTreeNodeIds(List<Long> ids, int companyId) throws Exception;

//	public List<CategoryTreeNode> queryCategoryTreesByLeftTreeNodeIds(List<Long> ids, int companyId) throws Exception;

	public List<CategoryTreeNodeRelation> queryCategoryRelationsByLeftCetgoryIds(List<Long> ids, int companyId) throws Exception;
	
	public List<ProductCategoryRelation> queryProductCategoryRelation(int companyId) throws Exception;

	List<MPCategoryRelation> queryMerchantProductCategory(List<Long> ids,int companyId) throws Exception;

	List<MPCategoryRelation> queryMerchantProductCategoryByCateIds(List<Long> ids,int companyId) throws Exception;

	List<MPCategoryRelation> queryAllMerchantProductCategory(int companyId) throws Exception;

	public List<Category> queryCategoriesByLeftCategoryIds(List<Long> ids, int companyId) throws Exception;
}
