package com.odianyun.search.whale.data.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.odianyun.search.whale.data.model.MerchantCateCategoryTreeNodeRel;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.data.dao.CategoryDao;
import com.odianyun.search.whale.data.dao.MerchantCategoryDao;
import com.odianyun.search.whale.data.model.Category;
import com.odianyun.search.whale.data.model.MerchantCateTreeNode;
import com.odianyun.search.whale.data.service.AbstractCompanyDBService;
import com.odianyun.search.whale.data.service.MerchantCategoryService;

public class MerchantCategoryServiceImpl extends AbstractCompanyDBService implements
		MerchantCategoryService {

	@Autowired
	CategoryDao categoryDao;
	@Autowired
	MerchantCategoryDao merchantCategoryDao;

	Map<Integer,MerchantCategoryCacheContext> merchantCategoryCacheContexts=new HashMap<Integer,MerchantCategoryCacheContext>();

	@Override
	protected void tryReload(int companyId) throws Exception {
		MerchantCategoryCacheContext merchantCategoryCacheContext=new MerchantCategoryCacheContext();
		merchantCategoryCacheContexts.put(companyId, merchantCategoryCacheContext);
		List<MerchantCateTreeNode> merchantTreeNodes = merchantCategoryDao.queryAllMerchantCateTreeNode(companyId);
		if (CollectionUtils.isNotEmpty(merchantTreeNodes)){
			Map<Long,MerchantCateTreeNode> merchantCateTreeNodeMap_temp
					= new ConcurrentHashMap<Long,MerchantCateTreeNode>();
			for(MerchantCateTreeNode merCateTreeNode:merchantTreeNodes){
				merchantCateTreeNodeMap_temp.put(merCateTreeNode.getId(), merCateTreeNode);
			}
			merchantCategoryCacheContext.merchantCateTreeNodeMap=merchantCateTreeNodeMap_temp;
		}
		List<MerchantCateCategoryTreeNodeRel> merchantCateCategoryTreeNodeRels=merchantCategoryDao.queryAllMerchantCateCateTreeNodeRel(companyId);
		if(CollectionUtils.isNotEmpty(merchantCateCategoryTreeNodeRels)){
			Map<String,List<Long>> merchantCateCategoryTreeNodeRelMap=new ConcurrentHashMap<String,List<Long>>();
            for(MerchantCateCategoryTreeNodeRel merchantCateCategoryTreeNodeRel:merchantCateCategoryTreeNodeRels){
				String key=merchantCateCategoryTreeNodeRel.getMerchantId()+"_"+merchantCateCategoryTreeNodeRel.getCateTreeNodeId();
				List<Long> refIds=merchantCateCategoryTreeNodeRelMap.get(key);
                if(refIds==null){
					refIds=new ArrayList<Long>();
					merchantCateCategoryTreeNodeRelMap.put(key,refIds);
				}
				refIds.add(merchantCateCategoryTreeNodeRel.getMerchantCateTreeNodeId());
			}
			merchantCategoryCacheContext.merchantCateCategoryTreeNodeRelMap=merchantCateCategoryTreeNodeRelMap;
		}
	}

	@Override
	public int getInterval() {
		return 30;
	}

	@Override
	public MerchantCateTreeNode getMerchantTreeNodeById(
			long merchant_cat_tree_node_id,int companyId) throws Exception {
		return merchantCategoryCacheContexts.get(companyId).merchantCateTreeNodeMap.get(merchant_cat_tree_node_id);
	}

	@Override
	public List<MerchantCateTreeNode> getFullPathMerchantCategoryByTreeNodeId(
			long merchant_cat_tree_node_id,int companyId) throws Exception {
		MerchantCategoryCacheContext merchantCategoryCacheContext=merchantCategoryCacheContexts.get(companyId);
		List<MerchantCateTreeNode> ret=new ArrayList<MerchantCateTreeNode>();
		MerchantCateTreeNode merchantTreeNode = merchantCategoryCacheContext.merchantCateTreeNodeMap.get(merchant_cat_tree_node_id);
		while(merchantTreeNode!=null){
			ret.add(merchantTreeNode);
			long parentId = merchantTreeNode.getParent_id();
			merchantTreeNode = merchantCategoryCacheContext.merchantCateTreeNodeMap.get(parentId);
		}
		return ret;
	}

	@Override
	public List<Long> getMerchantCategoryIdByCategoryIdMerchantId(Long categoryTreeNodeId, Long merchantId,int companyId) throws Exception {
		MerchantCategoryCacheContext merchantCategoryCacheContext=merchantCategoryCacheContexts.get(companyId);
		return merchantCategoryCacheContext.merchantCateCategoryTreeNodeRelMap.get(merchantId+"_"+categoryTreeNodeId);
	}

	@Override
	protected void tryReload(List<Long> ids,int companyId) throws Exception {
		MerchantCategoryCacheContext merchantCategoryCacheContext=merchantCategoryCacheContexts.get(companyId);
		if(merchantCategoryCacheContext == null){
			merchantCategoryCacheContext=new MerchantCategoryCacheContext();
			merchantCategoryCacheContexts.put(companyId, merchantCategoryCacheContext);
		}
		if(CollectionUtils.isEmpty(ids)){
			return;
		}
		List<MerchantCateTreeNode> merchantTreeNodes = merchantCategoryDao.getMerchantCateTreeNodes(ids,companyId);
		if (CollectionUtils.isNotEmpty(merchantTreeNodes)){
			for(MerchantCateTreeNode merCateTreeNode:merchantTreeNodes) {
				merchantCategoryCacheContext.merchantCateTreeNodeMap.put(merCateTreeNode.getId(), merCateTreeNode);
			}
		}
	}

	private static class MerchantCategoryCacheContext{

		Map<Long,MerchantCateTreeNode> merchantCateTreeNodeMap = new ConcurrentHashMap<Long,MerchantCateTreeNode>();
		Map<String,List<Long>> merchantCateCategoryTreeNodeRelMap=new ConcurrentHashMap<String,List<Long>>();

	}

}
