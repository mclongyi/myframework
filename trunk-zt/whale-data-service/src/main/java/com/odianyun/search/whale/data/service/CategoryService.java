package com.odianyun.search.whale.data.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.odianyun.search.whale.data.model.Category;
import com.odianyun.search.whale.data.model.CategoryTreeNode;

public interface CategoryService{
	/**
	 * 根据类目树节点ID获取类目路径
	 * @param categoryTreeNodeId
	 * @return
	 * @throws Exception
	 */
	/*public List<Category> getFullPathCategoryByTreeNodeId(Long categoryTreeNodeId,int companyId) throws Exception;*/
	public Map<Integer,List<String>> getAllCategorysName() throws Exception;
	/**
	 * 根据类目ID获取类目路径
	 * @param categoryId
	 * @return
	 * @throws Exception
	 */
	public List<Category> getFullPathCategory(Long categoryId,int companyId) throws Exception;
	
	/*public List<Long> getAllParentTreeId(Long categoryTreeId,int companyId);*/
	
	/**
	 * 根据类目树某个节点ID获取对应的类目
	 * @param categoryTreeNodeId
	 * @return
	 * @throws Exception
	 */
	/*public Category getCategoryByTreeNodeId(long categoryTreeNodeId,int companyId) throws Exception;*/
	
	/**
	 * 根据类目ID获取对应的类目树节点
	 * @param categoryId
	 * @return
	 * @throws Exception
	 */
	/*public CategoryTreeNode getCategoryTreeNodeById(long categoryId,int companyId) throws Exception;*/
	
	
	/**
	 * 根据类目ID获取对应的类目树节点路径
	 * @param categoryId
	 * @return
	 * @throws Exception
	 */
	/*public List<CategoryTreeNode> getFullPathCategoryTreeNodeById(long categoryId,int companyId);*/
	
	/**
	 * 根据类目ID批量获取对应的类目
	 * @param categoryIds
	 * @return
	 * @throws Exception
	 */
    public Map<Long,Category> getCategorys(List<Long> categoryIds,int companyId) throws Exception;
	
    /**
     * 根据类目树节点ID获取对应的节点
     * @param category_tree_node_id
     * @return
     * @throws Exception
     */
	/*public CategoryTreeNode getCategoryTreeNode(Long category_tree_node_id,int companyId) throws Exception;*/
	
	/**
	 * 根据类目ID获取对应的类目
	 * @param categoryId
	 * @return
	 * @throws Exception
	 */
	public Category getCategory(Long categoryId,int companyId) throws Exception;
	
	/**
	 * 根据类目ID获取其父类目ID
	 * @param categoryId
	 * @return
	 * @throws Exception
	 */
	public Long getParentCategoryId(long categoryId,int companyId) throws Exception;
	
/*	public List<Long> getLeftTreeNodeIds(Long rightTreeNodeId,int companyId) throws Exception;
*/
/*	public List<Category> getLeftCategorys(Long rightTreeNodeId,int companyId) throws Exception;
*/
	/**
	 * 根据后台类目Id获取对应的前台类目
	 */
	public List<Category> getLeftCategorysByCategoryId(Long categoryId,int companyId) throws Exception;
	
    public Collection<Category> getNaviCategorys(Long categoryId, Set<Long> excludeNaviCategoryIds, int companyId) throws Exception;
	
	public Collection<Category> getNaviCategorys(Long naviCategoryId, int companyId) throws Exception;
	
	public Set<Long> getExcludeNaviCategorys(Long productId, int companyId);
	
	public Set<Long> getIncludeNaviCategorys(Long productId, int companyId);

	/**
	 * 根据mpids获取商品关联前台类目
	 * */
	public Map<Long,Set<Category>> getNavicCategoryByMpIds(List<Long> ids,int companyId) throws Exception;

	/**
	 * 获取SeriesParentIds与其前台类目之间的关系
	 * @param SeriesParentIds
	 * @param companyId
	 * @return
	 * @throws Exception
	 */
	public Map<Long,Long> getNavicFrontCategoryIdByMpIds(List<Long> SeriesParentIds,int companyId) throws Exception;

	public Set<Long> getNavicCategoryIdsByMpIds(List<Long> ids,int companyId) throws Exception;

	public List<Category> getLeftCategorys(List<Long> categoryIds, int companyId) throws Exception;

	public List<Long> getAllParentCategoryId(Long categoryId, int companyId) throws Exception;

	public List<Category> getNaviCategorys(List<Long> categoryIds, Set<Long> excludeNaviCategoryIds, int companyId) throws Exception;

}
