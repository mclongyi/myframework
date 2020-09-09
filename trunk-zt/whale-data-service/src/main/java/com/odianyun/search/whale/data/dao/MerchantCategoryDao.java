package com.odianyun.search.whale.data.dao;

import java.util.List;

import com.odianyun.search.whale.data.model.MerchantCateCategoryTreeNodeRel;
import com.odianyun.search.whale.data.model.MerchantCateTreeNode;

public interface MerchantCategoryDao {

	public List<MerchantCateTreeNode> queryAllMerchantCateTreeNode(int companyId) throws Exception;
	
	public List<MerchantCateTreeNode> getMerchantCateTreeNodes(
			List<Long> merchant_cate_tree_node_ids, int companyId)throws Exception;
	
	public MerchantCateTreeNode getMerchantCateTreeNode(
			long merchant_cat_tree_node_id, int companyId)throws Exception;


	public List<MerchantCateCategoryTreeNodeRel> queryAllMerchantCateCateTreeNodeRel(int companyId) throws Exception;
	
}
