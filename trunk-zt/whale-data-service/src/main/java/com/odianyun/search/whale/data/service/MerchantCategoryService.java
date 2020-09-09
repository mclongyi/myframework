package com.odianyun.search.whale.data.service;

import java.util.List;

import com.odianyun.search.whale.data.model.Category;
import com.odianyun.search.whale.data.model.MerchantCateTreeNode;

public interface MerchantCategoryService {

	/**
	 * 根据NodeID获取对应的店铺类目树节点
	 * @param merchant_cat_tree_node_id
	 * @return
	 * @throws Exception
	 */
	public MerchantCateTreeNode getMerchantTreeNodeById(
			long merchant_cat_tree_node_id,int companyId) throws Exception;

	/**
	 * 根据NodeId获取对应的店铺类目树全路径
	 * @param merchant_cat_tree_node_id
	 * @return
	 * @throws Exception
	 */
	public List<MerchantCateTreeNode> getFullPathMerchantCategoryByTreeNodeId(long merchant_cat_tree_node_id,int companyId) throws Exception;

	public List<Long> getMerchantCategoryIdByCategoryIdMerchantId(Long categoryTreeNodeId,Long merchantId,int companyId) throws Exception;

}
