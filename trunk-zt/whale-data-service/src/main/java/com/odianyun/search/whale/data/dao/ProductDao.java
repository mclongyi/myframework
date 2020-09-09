package com.odianyun.search.whale.data.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.odianyun.search.whale.data.model.MerchantProdMerchantCateTreeNode;
import com.odianyun.search.whale.data.model.Product;

public interface ProductDao {
	
	/**
	 * 读取所有的产品数据,适用于全量索引
	 * @return
	 * @throws Exception
	 */
//	public List<Product> queryAllProducts(int companyId) throws Exception;
	
	/**
	 * 根据产品ID列表读取相关的商品信息，适用于实时索引
	 * @param productIds
	 * @return
	 * @throws Exception
	 */
	public List<Product> getProducts(List<Long> productIds, int companyId) throws Exception;
	
	public List<MerchantProdMerchantCateTreeNode> getMerchantCateTreeNodeIds(List<Long> merchantProductIds, int companyId) throws Exception;

	public List<Long> getProductsByCategoryTreeNodeIds(List<Long> arrayList, int companyId) throws Exception;

	public List<Long> getProductsByBrandIds(List<Long> brandIds, int companyId) throws Exception;

	public List<Long> getRightCategoryTreeNodeIds(List<Long> categoryTreeNodeIds, int companyId) throws Exception;

	public List<Long> getBrandsByProductIds(List<Long> productIdList, int companyId) throws Exception;
	/**
	 * 根据productId 从product关联查询 categoryTreeNodeId
	 * @param productIdList
	 * @param companyId
	 * @return
	 * @throws Exception
	 */
	public List<Long> getLeftCategoryIdsByProductIds(List<Long> productIdList, int companyId) throws Exception;
	/**
	 * 根据productId 从category_tree_node_product关联查询 categoryTreeNodeId
	 * @param productIdList
	 * @param companyId
	 * @return
	 * @throws Exception
	 */
	public List<Long> getLeftCategoryIdsByProductIds2(List<Long> productIdList, int companyId) throws Exception;


}
