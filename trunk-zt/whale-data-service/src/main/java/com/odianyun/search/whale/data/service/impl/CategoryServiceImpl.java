package com.odianyun.search.whale.data.service.impl;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.odianyun.search.whale.data.model.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.data.dao.CategoryDao;
import com.odianyun.search.whale.data.service.AbstractCompanyDBService;
import com.odianyun.search.whale.data.service.CategoryService;

public class CategoryServiceImpl extends AbstractCompanyDBService implements CategoryService {

    Map<Long, Category> categoryMap = new ConcurrentHashMap<Long, Category>();
    @Autowired
    CategoryDao categoryDao;

    static Logger logger = Logger.getLogger(CategoryServiceImpl.class);

    Map<Integer, CategoryCacheContext> categoryCacheContexts = new ConcurrentHashMap<Integer, CategoryCacheContext>();

    private enum CategoryType {
        Null,
        AllAggregation,
        PartAggregation,
        Include,
        Exclude
    }

    @Override
    public Category getCategory(Long categoryId, int companyId) {
        Category ret = null;
        CategoryCacheContext context = categoryCacheContexts.get(companyId);
        if (context != null) {
            ret = context.categoryMap.get(categoryId);
        }
        return ret;
    }

    @Override
    public Map<Integer, List<String>> getAllCategorysName() throws Exception {
        Map<Integer, List<String>> allCatrgoryName = new HashMap<>();
        for (Map.Entry<Integer, CategoryCacheContext> c : categoryCacheContexts.entrySet()) {
            List<String> tmp = new ArrayList<>();
            allCatrgoryName.put(c.getKey(), tmp);
            for (Map.Entry<Long, Category> cc : c.getValue().categoryMap.entrySet()) {
                tmp.add(cc.getValue().getName());
            }
        }
        return allCatrgoryName;
    }
	/*@Override
	public List<Category> getFullPathCategoryByTreeNodeId(Long categoryTreeId,int companyId){		
		List<Category> ret=new ArrayList<Category>();
		CategoryCacheContext context=categoryCacheContexts.get(companyId);
		if(context!=null){
			CategoryTreeNode treeNode=context.treeNodeMap.get(categoryTreeId);
			while(treeNode!=null){
				long categoryId = treeNode.getCategory_id();
				Category cate = context.categoryMap.get(categoryId);
				if(cate!=null)
					ret.add(cate);
				Long parentId = treeNode.getParent_id();
				treeNode=context.treeNodeMap.get(parentId);
			}
		}
		return ret;
	}*/
	
	/*@Override
	public List<Long> getAllParentTreeId(Long categoryTreeId,int companyId){
		List<Long> ret=new ArrayList<Long>();
		CategoryCacheContext context=categoryCacheContexts.get(companyId);
		if(context!=null){
			Long ctid=categoryTreeId;
			while(ctid!=null&&ctid!=0){
				ret.add(ctid);
				ctid=context.categoryTreeId2PraentId.get(ctid);
			}
		}
		return ret;
	}*/

    public void tryReload(int companyId) throws Exception {
        CategoryCacheContext categoryCacheContext = new CategoryCacheContext();
        List<Category> categories = categoryDao.queryAllCategory(companyId);
//		List<CategoryTreeNode> treeNodes=categoryDao.queryAllCategoryTree(companyId);
        Map<Long, Category> categoryMap_temp = new ConcurrentHashMap<Long, Category>();
        Map<Long, Long> categoryId2PraentId_temp = new ConcurrentHashMap<Long, Long>();
        for (Category c : categories) {
            String fullIdPath = c.getFullIdPath();
            if (StringUtils.isNotBlank(fullIdPath)) {
                List<Long> fullIdPathList = genFullIdPathList(fullIdPath);
                c.setFullIdPathList(fullIdPathList);
            }
            categoryId2PraentId_temp.put(c.getId(), c.getParentId());
            categoryMap_temp.put(c.getId(), c);
        }
        categoryCacheContext.categoryMap = categoryMap_temp;
        categoryCacheContext.categoryId2PraentId = categoryId2PraentId_temp;

        List<CategoryTreeNodeRelation> treeNodeRelations = categoryDao.queryAllCategoryRelation(companyId);
        if (treeNodeRelations != null) {
            Map<Long, List<Long>> rightCategoryId2Left_temp = new ConcurrentHashMap<Long, List<Long>>();
            Map<Long, List<Long>> leftCategoryId2right_temp = new ConcurrentHashMap<Long, List<Long>>();
            for (CategoryTreeNodeRelation ctnr : treeNodeRelations) {
                List<Long> leftCategoryList = rightCategoryId2Left_temp.get(ctnr.getRightCategoryTreeNodeId());
                if (leftCategoryList == null) {
                    leftCategoryList = new ArrayList<Long>();
                    rightCategoryId2Left_temp.put(ctnr.getRightCategoryTreeNodeId(), leftCategoryList);
                }
                leftCategoryList.add(ctnr.getLeftCategoryTreeNodeId());
                List<Long> rightCategoryList = leftCategoryId2right_temp.get(ctnr.getLeftCategoryTreeNodeId());
                if (rightCategoryList == null) {
                    rightCategoryList = new ArrayList<Long>();
                    rightCategoryId2Left_temp.put(ctnr.getLeftCategoryTreeNodeId(), rightCategoryList);
                }
                rightCategoryList.add(ctnr.getRightCategoryTreeNodeId());
            }
            categoryCacheContext.rightCategoryId2Left = rightCategoryId2Left_temp;
            categoryCacheContext.leftCategoryId2right = leftCategoryId2right_temp;
        }

        loadProductCategoryRelation(companyId, categoryCacheContext);
        //loadMPCategoryRelation(companyId,categoryCacheContext);
        categoryCacheContexts.put(companyId, categoryCacheContext);
    }

	/*private void loadMPCategoryRelation(int companyId, CategoryCacheContext categoryCacheContext) throws Exception{
		List<MPCategoryRelation> relationList = categoryDao.queryAllMerchantProductCategory(companyId);
		Map<Long,List<MPCategoryRelation>> tmp = new HashMap<Long,List<MPCategoryRelation>>();
 		if(relationList!=null && CollectionUtils.isNotEmpty(relationList)){
			for(MPCategoryRelation mp:relationList){
				List<MPCategoryRelation> list = tmp.get(mp.getMpId());
				if(list == null){
					list = new ArrayList<MPCategoryRelation>();
				}
				list.add(mp);
				tmp.put(mp.getMpId(),list);
			}
		}
		if(tmp.size()>0){
			categoryCacheContext.MPCategoryRelations = tmp;
		}
	}*/

    private List<Long> genFullIdPathList(String fullIdPath) {
        List<Long> fullIdPathList = new ArrayList<>();
        String[] ids = fullIdPath.split("-");
        if (ids != null && ids.length > 0) {
            for (String id : ids) {
                if (StringUtils.isNotBlank(id)) {
                    try {
                        fullIdPathList.add(Long.valueOf(id.trim()));
                    } catch (NumberFormatException e) {

                    }
                }
            }
        }
        return fullIdPathList;
    }

    private void loadProductCategoryRelation(int companyId, CategoryCacheContext categoryCacheContext) throws Exception {
        List<ProductCategoryRelation> productCategoryRelations = categoryDao.queryProductCategoryRelation(companyId);
        if (productCategoryRelations != null) {
            Map<Long, Set<Long>> excludeProductNaviCategoryRelations_temp = new HashMap<Long, Set<Long>>();
            Map<Long, Set<Long>> includeProductNaviCategoryRelations_temp = new HashMap<Long, Set<Long>>();
            for (ProductCategoryRelation relation : productCategoryRelations) {
                if (relation.getType() == CategoryType.Include.ordinal()) {
                    Set<Long> naviCategories = includeProductNaviCategoryRelations_temp.get(relation.getProductId());
                    if (naviCategories == null) {
                        naviCategories = new HashSet<Long>();
                        includeProductNaviCategoryRelations_temp.put(relation.getProductId(), naviCategories);
                    }
                    naviCategories.add(relation.getNaviCategoryId());
                } else if (relation.getType() == CategoryType.Exclude.ordinal()) {
                    Set<Long> naviCategories = excludeProductNaviCategoryRelations_temp.get(relation.getProductId());
                    if (naviCategories == null) {
                        naviCategories = new HashSet<Long>();
                        excludeProductNaviCategoryRelations_temp.put(relation.getProductId(), naviCategories);
                    }
                    naviCategories.add(relation.getNaviCategoryId());
                }
            }//end for
            categoryCacheContext.excludeProductNaviCategoryRelations = excludeProductNaviCategoryRelations_temp;
            categoryCacheContext.includeProductNaviCategoryRelations = includeProductNaviCategoryRelations_temp;
        }
    }

    @Override
    public Map<Long, Category> getCategorys(List<Long> categoryIds, int companyId) throws Exception {
        Map<Long, Category> retMap = new HashMap<Long, Category>();
        for (Long categoryId : categoryIds) {
            Category category = categoryCacheContexts.get(companyId).categoryMap.get(categoryId);
            if (category != null)
                retMap.put(categoryId, category);
        }
        return retMap;
    }

	/*@Override
	public CategoryTreeNode getCategoryTreeNode(Long category_tree_node_id,int companyId)
			throws Exception {
		return categoryCacheContexts.get(companyId).treeNodeMap.get(category_tree_node_id);
	}*/
	
	/*@Override
	public Category getCategoryByTreeNodeId(long categoryTreeNodeId,int companyId) throws Exception{
		Long cid=categoryCacheContexts.get(companyId).categoryTreeId2Id.get(categoryTreeNodeId);
		if(null != cid){
			return categoryCacheContexts.get(companyId).categoryMap.get(cid);
		}
		return null;
	}*/

    @Override
    public List<Category> getFullPathCategory(Long categoryId, int companyId) throws Exception {
        List<Category> fullPathCategorys = new ArrayList<>();
        CategoryCacheContext categoryCacheContext = categoryCacheContexts.get(companyId);
        if (categoryCacheContext == null) {
            return fullPathCategorys;
        }
        Category category = categoryCacheContext.categoryMap.get(categoryId);
        if (category == null) {
            return fullPathCategorys;
        }
        fullPathCategorys.add(category);
        List<Long> fullIdPathList = getAllParentCategoryId(categoryId, companyId);
        if (CollectionUtils.isNotEmpty(fullIdPathList)) {
            for (Long cateId : fullIdPathList) {
                Category categoryTemp = categoryCacheContext.categoryMap.get(cateId);
                if (categoryTemp != null) {
                    fullPathCategorys.add(categoryTemp);
                }
            }
        }
        return fullPathCategorys;
    }

	/*@Override
	public CategoryTreeNode getCategoryTreeNodeById(long categoryId,int companyId)
			throws Exception {
		CategoryCacheContext context=categoryCacheContexts.get(companyId);
		Long category_treeNode_id=context.categoryId2TreeId.get(categoryId);
		return context.treeNodeMap.get(category_treeNode_id);
	}*/

    @Override
    public Long getParentCategoryId(long categoryId, int companyId) throws Exception {
		/*CategoryCacheContext context=categoryCacheContexts.get(companyId);
		Long treeNodeId = context.categoryId2TreeId.get(categoryId);
		if(treeNodeId!=null){
			Long parentTreeNodeId = context.categoryTreeId2PraentId.get(treeNodeId);
			if(parentTreeNodeId!=null)
				return context.categoryTreeId2Id.get(parentTreeNodeId);
		}
		return null;*/
        CategoryCacheContext context = categoryCacheContexts.get(companyId);
        if (context != null) {
            return context.categoryId2PraentId.get(categoryId);
        }
        return null;
    }

    public int getInterval() {
        return 30;
    }

	/*@Override
	public List<Long> getLeftTreeNodeIds(Long rightTreeNodeId,int companyId) throws Exception {
		return categoryCacheContexts.get(companyId).rightCategoryId2Left.get(rightTreeNodeId);
	}*/

	/*@Override
	public List<Category> getLeftCategorys(Long rightTreeNodeId,int companyId)
			throws Exception {
		List<Long> leftTreeNodeIds=getLeftTreeNodeIds(rightTreeNodeId,companyId);
		if(leftTreeNodeIds==null){
			return null;
		}
		Set<Category> sets=new HashSet<Category>();
		for(Long treeNodeId:leftTreeNodeIds){
		    List<Category> categorys=getFullPathCategoryByTreeNodeId(treeNodeId,companyId);
		    if(categorys!=null){
		    	sets.addAll(categorys);
		    }
		}
		return new ArrayList<Category>(sets);
	}*/

    @Override
    public List<Category> getLeftCategorysByCategoryId(Long categoryId, int companyId)
            throws Exception {
		/*Map<Long,Category> categories=new HashMap<Long,Category>();
		Long categoryTreeNodeId=categoryCacheContexts.get(companyId).categoryId2TreeId.get(categoryId);
		if(categoryTreeNodeId!=null){
			List<Long> leftTreeNodeIds=getLeftTreeNodeIds(categoryTreeNodeId,companyId);
			if(CollectionUtils.isNotEmpty(leftTreeNodeIds)){
				for(Long treeNodeId:leftTreeNodeIds){
					Category cate=getCategoryByTreeNodeId(treeNodeId,companyId);
					if(cate!=null){
						categories.put(cate.getId(), cate);
					}
				}
			}
		}
		return new LinkedList<Category>(categories.values());
		*/
        List<Category> categoryList = new ArrayList<>();
        List<Long> leftCategoryIds = categoryCacheContexts.get(companyId).rightCategoryId2Left.get(categoryId);
        if (CollectionUtils.isNotEmpty(leftCategoryIds)) {
            for (Long id : leftCategoryIds) {
                Category category = categoryCacheContexts.get(companyId).categoryMap.get(id);
                if (category != null) {
                    categoryList.add(category);
                }
            }
        }
        return categoryList;

    }

	/*@Override
	public List<CategoryTreeNode> getFullPathCategoryTreeNodeById(
			long categoryId,int companyId) {
		CategoryCacheContext context=categoryCacheContexts.get(companyId);
		List<CategoryTreeNode> categorieTreeNodes=new LinkedList<CategoryTreeNode>();
		Long categoryTreeNodeId=context.categoryId2TreeId.get(categoryId);
		while(categoryTreeNodeId!=null && !categoryTreeNodeId.equals("0")){
			CategoryTreeNode categoryTreeNode=context.treeNodeMap.get(categoryTreeNodeId);
			if(categoryTreeNode!=null){
				categorieTreeNodes.add(categoryTreeNode);
				Long parent_id=categoryTreeNode.getParent_id();
				if(categoryTreeNodeId.equals(parent_id)){
					break;
				}else{
					categoryTreeNodeId=parent_id;
				}
			}else{
				break;
			}
		}
		return categorieTreeNodes;
	}*/

    @Override
    protected void tryReload(List<Long> ids, int companyId) throws Exception {
        CategoryCacheContext context = categoryCacheContexts.get(companyId);
        if (context == null) {
            context = new CategoryCacheContext();
            categoryCacheContexts.put(companyId, context);
        }
        List<Category> categories = categoryDao.queryCategoriesByLeftCategoryIds(ids, companyId);
        if (CollectionUtils.isNotEmpty(categories)) {
            for (Category c : categories) {
                String fullIdPath = c.getFullIdPath();
                if (StringUtils.isNotBlank(fullIdPath)) {
                    List<Long> fullIdPathList = genFullIdPathList(fullIdPath);
                    c.setFullIdPathList(fullIdPathList);
                }
                context.categoryMap.put(c.getId(), c);
            }
        }
		
		/*List<CategoryTreeNode> treeNodes=categoryDao.queryCategoryTreesByLeftTreeNodeIds(ids,companyId);
		if(CollectionUtils.isNotEmpty(treeNodes)){
			for(CategoryTreeNode treeNode:treeNodes){
				context.treeNodeMap.put(treeNode.getId(), treeNode);
				context.categoryTreeId2PraentId.put(treeNode.getId(),treeNode.getParent_id());
				context.categoryTreeId2Id.put(treeNode.getId(), treeNode.getCategory_id());
				context.categoryId2TreeId.put(treeNode.getCategory_id(),treeNode.getId());
			}
		}*/

        //CategoryTreeNodeRelation 已经改为 CategoryRelation
        List<CategoryTreeNodeRelation> treeNodeRelations = categoryDao.queryCategoryRelationsByLeftCetgoryIds(ids, companyId);
        if (CollectionUtils.isNotEmpty(treeNodeRelations)) {
            Map<Long, List<Long>> rightCategoryId2Left_temp = new HashMap<Long, List<Long>>();
            Map<Long, List<Long>> leftCategoryId2right_temp = new HashMap<Long, List<Long>>();
            for (CategoryTreeNodeRelation ctnr : treeNodeRelations) {
                List<Long> leftNodeList = rightCategoryId2Left_temp.get(ctnr.getRightCategoryTreeNodeId());
                if (leftNodeList == null) {
                    leftNodeList = new ArrayList<Long>();
                    rightCategoryId2Left_temp.put(ctnr.getRightCategoryTreeNodeId(), leftNodeList);
                }
                leftNodeList.add(ctnr.getLeftCategoryTreeNodeId());
                List<Long> rightNodeList = leftCategoryId2right_temp.get(ctnr.getLeftCategoryTreeNodeId());
                if (rightNodeList == null) {
                    rightNodeList = new ArrayList<Long>();
                    rightCategoryId2Left_temp.put(ctnr.getLeftCategoryTreeNodeId(), rightNodeList);
                }
                rightNodeList.add(ctnr.getRightCategoryTreeNodeId());
            }
            if (rightCategoryId2Left_temp.size() > 0) {
                context.rightCategoryId2Left.putAll(rightCategoryId2Left_temp);
            }
            if (leftCategoryId2right_temp.size() > 0) {
                context.leftCategoryId2right.putAll(leftCategoryId2right_temp);
            }
        }
        //实时索引merchantProductCategory
		/*List<MPCategoryRelation> relationList = categoryDao.queryMerchantProductCategoryByCateIds(ids,companyId);
		Map<Long,List<MPCategoryRelation>> tmp = new HashMap<Long,List<MPCategoryRelation>>();
		Map<Long,List<MPCategoryRelation>> sourceMap = context.MPCategoryRelations;
		if(relationList!=null && CollectionUtils.isNotEmpty(relationList)){
			for(MPCategoryRelation mp:relationList){
				List<MPCategoryRelation> list = tmp.get(mp.getMpId());
				if(list == null){
					list = new ArrayList<MPCategoryRelation>();
				}
				list.add(mp);
				tmp.put(mp.getMpId(),list);
			}
		}
		for(Long id:ids){
			if(tmp.containsKey(id)){
				sourceMap.put(id,tmp.get(id));
			}else {
				sourceMap.remove(id);
			}
		}
		context.MPCategoryRelations = sourceMap;*/


    }


    private static class CategoryCacheContext {

        Map<Long, Category> categoryMap = new ConcurrentHashMap<Long, Category>();

//		Map<Long,CategoryTreeNode> treeNodeMap =new ConcurrentHashMap<Long,CategoryTreeNode>();

        Map<Long, Long> categoryId2PraentId = new ConcurrentHashMap<Long, Long>();

//		Map<Long,Long> categoryTreeId2Id=new ConcurrentHashMap<Long,Long>();

//		Map<Long,Long> categoryId2TreeId=new ConcurrentHashMap<Long,Long>();

        Map<Long, List<Long>> rightCategoryId2Left = new ConcurrentHashMap<Long, List<Long>>();

        Map<Long, List<Long>> leftCategoryId2right = new ConcurrentHashMap<Long, List<Long>>();

        Map<Long, Set<Long>> excludeProductNaviCategoryRelations = new HashMap<Long, Set<Long>>();
        Map<Long, Set<Long>> includeProductNaviCategoryRelations = new HashMap<Long, Set<Long>>();

        //Map<Long,List<MPCategoryRelation>> MPCategoryRelations = new HashMap<Long,List<MPCategoryRelation>>();
    }


    @Override
    public Collection<Category> getNaviCategorys(Long categoryId, Set<Long> excludeNaviCategoryIds,
                                                 int companyId) throws Exception {
		/*List<Long> leftTreeNodeIds=getLeftTreeNodeIds(categoryId, companyId);
		if(leftTreeNodeIds==null){
			return null;
		}
		Set<Category> sets=new HashSet<Category>();
		for(Long treeNodeId : leftTreeNodeIds){
			if(excludeNaviCategoryIds.contains(treeNodeId)) {
				continue;
			}
		    List<Category> categorys=getFullPathCategoryByTreeNodeId(treeNodeId, companyId);
		    if(categorys!=null){
		    	sets.addAll(categorys);
		    }
		}*/
        Set<Category> sets = new HashSet<Category>();
        List<Long> leftCategoryIdList = categoryCacheContexts.get(companyId).rightCategoryId2Left.get(categoryId);
        if (CollectionUtils.isNotEmpty(leftCategoryIdList)) {
            for (Long leftCategoryId : leftCategoryIdList) {
                if (excludeNaviCategoryIds.contains(leftCategoryId)) {
                    continue;
                }
                List<Category> categorys = getFullPathCategory(leftCategoryId, companyId);
                if (categorys != null) {
                    sets.addAll(categorys);
                }
            }
        }
        return sets;
    }

    @Override
    public Collection<Category> getNaviCategorys(Long naviCategoryId, int companyId) throws Exception {
        Set<Category> sets = new HashSet<Category>();
        List<Category> categorys = getFullPathCategory(naviCategoryId, companyId);
        if (categorys != null) {
            sets.addAll(categorys);
        }
        return sets;
    }

    @Override
    public Set<Long> getExcludeNaviCategorys(Long productId, int companyId) {
        CategoryCacheContext context = categoryCacheContexts.get(companyId);
        return context.excludeProductNaviCategoryRelations.get(productId);
    }

    @Override
    public Set<Long> getIncludeNaviCategorys(Long productId, int companyId) {
        CategoryCacheContext context = categoryCacheContexts.get(companyId);
        return context.includeProductNaviCategoryRelations.get(productId);
    }

    @Override
    public Map<Long, Set<Category>> getNavicCategoryByMpIds(List<Long> ids, int companyId) throws Exception {
        Map<Long, Set<Category>> retMap = new HashMap<Long, Set<Category>>();
        if (ids.isEmpty()) {
            return retMap;
        }
		/*Map<Long,List<MPCategoryRelation>> sourceMap = categoryCacheContexts.get(companyId).MPCategoryRelations;
		List<MPCategoryRelation> listRelation = new ArrayList<MPCategoryRelation>();
		for(Long id:ids){
			if(sourceMap.containsKey(id)){
				listRelation.addAll(sourceMap.get(id));
			}
		}*/
        List<MPCategoryRelation> listRelation = categoryDao.queryMerchantProductCategory(ids, companyId);
        if (listRelation == null) {
            return retMap;
        }
        if (CollectionUtils.isNotEmpty(listRelation)) {
            for (MPCategoryRelation re : listRelation) {
                Set<Category> tmpCate = retMap.get(re.getMpId());
                if (tmpCate == null) {
                    tmpCate = new HashSet<Category>();
                    retMap.put(re.getMpId(), tmpCate);
                }
                List<Category> cateList = getFullPathCategory(re.getCategoryId(), companyId);
                if (CollectionUtils.isNotEmpty(cateList)) {
                    tmpCate.addAll(cateList);
                }
            }
        }
        return retMap;
    }

    @Override
    public Map<Long, Long> getNavicFrontCategoryIdByMpIds(List<Long> seriesParentIds, int companyId) throws Exception {
        Map<Long, Long> retMap = new HashMap<>();
        if (seriesParentIds.isEmpty()) {
            return retMap;
        }
        List<MPCategoryRelation> listRelation = categoryDao.queryMerchantProductCategory(seriesParentIds, companyId);
        if (CollectionUtils.isNotEmpty(listRelation)) {
            for (MPCategoryRelation relation : listRelation) {
                retMap.put(relation.getMpId(), relation.getCategoryId());
            }
        }
        return retMap;
    }
	/*@Override
	public Map<Long, Set<Category>> getNavicCategoryByMpIds(List<Long> ids, int companyId) throws Exception {
		Map<Long,Set<Category>> retMap = new HashMap<Long,Set<Category>>();
		Map<Long,List<MPCategoryRelation>> sourceMap = categoryCacheContexts.get(companyId).MPCategoryRelations;
		List<MPCategoryRelation> listRelation = new ArrayList<MPCategoryRelation>();
		for(Long id:ids){
			if(sourceMap.containsKey(id)){
				listRelation.addAll(sourceMap.get(id));
			}
		}
		List<MPCategoryRelation> listRelation = categoryDao.queryMerchantProductCategory(ids,companyId);
		if(listRelation == null){
			return retMap;
		}
		if(CollectionUtils.isNotEmpty(listRelation)){
			for(MPCategoryRelation re : listRelation){
				Set<Category> tmpCate = retMap.get(re.getMpId());
				if(tmpCate == null){
					tmpCate = new HashSet<Category>();
					retMap.put(re.getMpId(),tmpCate);
				}
				CategoryTreeNode categoryTreeNode = getCategoryTreeNode(re.getCategoryId(),companyId);
				if(categoryTreeNode!=null) {
					List<Category> cateList = getFullPathCategory(categoryTreeNode.getCategory_id(), companyId);
					if (CollectionUtils.isNotEmpty(cateList)) {
						tmpCate.addAll(cateList);
					}
				}
			}
		}
		return retMap;
	}*/

    @Override
    public Set<Long> getNavicCategoryIdsByMpIds(List<Long> ids, int companyId) throws Exception {
        Set<Long> retSet = new HashSet<Long>();
        Set<Long> mpIds = new HashSet<Long>();
        List<MPCategoryRelation> relationList = categoryDao.queryMerchantProductCategory(ids, companyId);
        if (CollectionUtils.isNotEmpty(relationList)) {
            for (MPCategoryRelation m : relationList) {
                retSet.add(m.getCategoryId());
                mpIds.add(m.getMpId());
            }
        }
        return retSet;
    }

    @Override
    public List<Category> getLeftCategorys(List<Long> categoryIds, int companyId) throws Exception {
        List<Category> leftCategorys = new ArrayList<>();
        if (CollectionUtils.isEmpty(categoryIds)) {
            return leftCategorys;
        }
        for (Long categoryId : categoryIds) {
            List<Long> leftCategoryIds = categoryCacheContexts.get(companyId).rightCategoryId2Left.get(categoryId);
            if (CollectionUtils.isNotEmpty(leftCategoryIds)) {
                Map<Long, Category> leftCategoryMap = getCategorys(leftCategoryIds, companyId);
                if (leftCategoryMap != null && leftCategoryMap.size() > 0) {
                    leftCategorys.addAll(leftCategoryMap.values());
                }
            }
        }
        return leftCategorys;
    }

    @Override
    public List<Long> getAllParentCategoryId(Long categoryId, int companyId) throws Exception {
        List<Long> allParentCategoryIds = new ArrayList<>();
        Category category = categoryCacheContexts.get(companyId).categoryMap.get(categoryId);
        if (category != null) {
            Long parentId = getParentCategoryId(categoryId, companyId);
            while (parentId != null && parentId > 0) {
                allParentCategoryIds.add(parentId);
                parentId = getParentCategoryId(parentId, companyId);
            }
			/*
			List<Long> fullIdPathList = category.getFullIdPathList();
			if(CollectionUtils.isNotEmpty(fullIdPathList)){
				for(Long cateId : fullIdPathList){
					if(cateId != categoryId){
						allParentCategoryIds.add(cateId);
					}
				}
			}*/
        }
        return allParentCategoryIds;
    }

    @Override
    public List<Category> getNaviCategorys(List<Long> categoryIds, Set<Long> excludeNaviCategoryIds, int companyId)
            throws Exception {
        List<Category> naviCategorys = new ArrayList<>();
        if (CollectionUtils.isEmpty(categoryIds)) {
            return naviCategorys;
        }

        for (Long categoryId : categoryIds) {
            Collection<Category> list = getNaviCategorys(categoryId, excludeNaviCategoryIds, companyId);
            if (CollectionUtils.isNotEmpty(list)) {
                naviCategorys.addAll(list);
            }
        }
        return naviCategorys;
    }

}
