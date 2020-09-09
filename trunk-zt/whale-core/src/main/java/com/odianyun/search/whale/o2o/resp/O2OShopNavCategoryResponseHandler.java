package com.odianyun.search.whale.o2o.resp;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.MerchantProduct;
import com.odianyun.search.whale.api.model.o2o.O2OShopCategoryResponse;
import com.odianyun.search.whale.api.model.o2o.O2OShopSearchResponse;
import com.odianyun.search.whale.data.model.Category;
import com.odianyun.search.whale.data.service.CategoryService;

public class O2OShopNavCategoryResponseHandler implements O2OShopSearchResponseHandler{
	
	static Logger log = Logger.getLogger(O2OShopNavCategoryResponseHandler.class);

	@Autowired
	CategoryService categoryService;

	@Override
	public void handle(SearchResponse searchResponse,
			O2OShopSearchResponse o2oShopSearchResponse) throws SearchException {
		final int companyId=o2oShopSearchResponse.getCompanyId();
		Map<Long,O2OShopCategoryResponse> o2OShopNavCategoryResponses=new HashMap<Long,O2OShopCategoryResponse>();	
		O2OShopCategoryResponse NotFoundNavCategoryResponse=new O2OShopCategoryResponse();
		List<O2OShopCategoryResponse> o2OShopCategoryResponses=o2oShopSearchResponse.getShopCategoryResponse();
		for(O2OShopCategoryResponse o2oShopCategoryResponse:o2OShopCategoryResponses){
			List<MerchantProduct> merchantProducts=o2oShopCategoryResponse.getMerchantProducts();
			for(MerchantProduct mp:merchantProducts){
				Long categoryId=mp.getCategoryId();
				if(categoryId!=null){
					try{
						List<Category> categories=categoryService.getLeftCategorysByCategoryId(categoryId,companyId); 
						if(CollectionUtils.isNotEmpty(categories)){
							Set<String> categoryNameSet=new HashSet<String>();
							for(Category category:categories){
								if(categoryNameSet.contains(category.getName())){
									continue;
								}
								categoryNameSet.add(category.getName());								
								O2OShopCategoryResponse o2OShopNavCategoryResponse=o2OShopNavCategoryResponses.get(category.getId());
								if(o2OShopNavCategoryResponse==null){
									o2OShopNavCategoryResponse=new O2OShopCategoryResponse();
									o2OShopNavCategoryResponse.setMerchantCategoryId(category.getId());
									o2OShopNavCategoryResponse.setMerchantCategoryName(category.getName());
									o2OShopNavCategoryResponses.put(category.getId(), o2OShopNavCategoryResponse);
								}
								o2OShopNavCategoryResponse.getMerchantProducts().add(mp);
							}
						}else{
							NotFoundNavCategoryResponse.getMerchantProducts().add(mp);
						}
					}catch(Exception e){
						throw new SearchException(e.getMessage(),e);
					}
					
				}else{
					NotFoundNavCategoryResponse.getMerchantProducts().add(mp);
				}
			}
		}
		List<O2OShopCategoryResponse> resultList=new LinkedList(o2OShopNavCategoryResponses.values());
		Collections.sort(resultList, new Comparator<O2OShopCategoryResponse>() {

			@Override
			public int compare(O2OShopCategoryResponse o1,
					O2OShopCategoryResponse o2) {
				LinkedList<Category> category1s = null;
				LinkedList<Category> category2s = null;
				try {
					category1s = (LinkedList<Category>)
							categoryService.getFullPathCategory(o1.getMerchantCategoryId(),companyId);
					category2s = (LinkedList<Category>)
							categoryService.getFullPathCategory(o2.getMerchantCategoryId(),companyId);

				} catch (Exception e) {
					log.error("categoryService getFullPathCategory error " + e.getMessage(), e);
				}

				if(CollectionUtils.isEmpty(category1s)){
					return 1;
				}
				if(CollectionUtils.isEmpty(category2s)){
					return -1;
				}

				if(category1s.size()>category2s.size()){
					LinkedList<Category> category1s_new=new LinkedList<Category>();
					for(int i=0;i<category2s.size();i++){
						category1s_new.add(category1s.removeLast());
					}
					category1s=category1s_new;
				}
				if(category1s.size()<category2s.size()){
					LinkedList<Category> category2s_new=new LinkedList<Category>();
					for(int i=0;i<category1s.size();i++){
						category2s_new.add(category2s.removeLast());
					}
					category2s=category2s_new;
				}
				while(category1s.size()>0){
					Category treeNode1=category1s.removeLast();
					Category treeNode2=category2s.removeLast();
					if(treeNode1.getListSort()>treeNode2.getListSort()){
						return 1;
					}else if(treeNode1.getListSort()<treeNode2.getListSort()){
						return -1;
					}
				}
				return 0;
			}
			/*@Override
			public int compare(O2OShopCategoryResponse o1,
					O2OShopCategoryResponse o2) {
				LinkedList<CategoryTreeNode> treeNode1s=(LinkedList<CategoryTreeNode>)
						categoryService.getFullPathCategoryTreeNodeById(o1.getMerchantCategoryId(),companyId);
				LinkedList<CategoryTreeNode> treeNode2s=(LinkedList<CategoryTreeNode>) 
						categoryService.getFullPathCategoryTreeNodeById(o2.getMerchantCategoryId(),companyId);		
				
				if(CollectionUtils.isEmpty(treeNode1s)){
					return 1;
				}
				if(CollectionUtils.isEmpty(treeNode2s)){
					return -1;
				}
				
				if(treeNode1s.size()>treeNode2s.size()){
					LinkedList<CategoryTreeNode> treeNode1s_new=new LinkedList<CategoryTreeNode>();
					for(int i=0;i<treeNode2s.size();i++){
						treeNode1s_new.add(treeNode1s.removeLast());
					}
					treeNode1s=treeNode1s_new;
				}
				if(treeNode1s.size()<treeNode2s.size()){
					LinkedList<CategoryTreeNode> treeNode2s_new=new LinkedList<CategoryTreeNode>();
					for(int i=0;i<treeNode1s.size();i++){
						treeNode2s_new.add(treeNode2s.removeLast());
					}
					treeNode2s=treeNode2s_new;
				}
				while(treeNode1s.size()>0){
					CategoryTreeNode treeNode1=treeNode1s.removeLast();
					CategoryTreeNode treeNode2=treeNode2s.removeLast();
					if(treeNode1.getListSort()>treeNode2.getListSort()){
						return 1;
					}else if(treeNode1.getListSort()<treeNode2.getListSort()){
						return -1;
					}
				}
				return 0;
			}*/
			
		});
		o2oShopSearchResponse.setNavCategoryResponse(resultList);
		if(CollectionUtils.isNotEmpty(NotFoundNavCategoryResponse.getMerchantProducts())){
			resultList.add(NotFoundNavCategoryResponse);
		}
		
	}
	
	public static void main(String[] args){
		List<Long> ids=new LinkedList<Long>();
		ids.add(1l);
		ids.add(0l);
		ids.add(-1l);
		ids.add(-1l);
		ids.add(5l);
		ids.add(-3l);
		List<Long> ids2=new LinkedList<Long>();
		ids2.add(5l);
		ids2.add(4l);
		ids2.add(1l);
		ids2.add(0l);
		ids2.add(-1l);
		System.out.println(ids2);
		Collections.sort(ids2,new Comparator<Long>() {

			@Override
			public int compare(Long o1, Long o2) {
				if(o1<o2){
					return 1;
				}else if(o1>o2){
					return -1;
				}
				return 0;
			}
		});

		Collections.sort(ids);
		System.out.println(ids);
		System.out.println(ids2);

		
	} 
	

}
