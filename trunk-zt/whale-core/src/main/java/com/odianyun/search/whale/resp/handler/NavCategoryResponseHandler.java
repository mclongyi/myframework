package com.odianyun.search.whale.resp.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.odianyun.search.whale.api.model.req.BaseSearchRequest;

import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.bucket.terms.InternalTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.api.model.CategoryTreeResult;
import com.odianyun.search.whale.api.model.SearchResponse;
import com.odianyun.search.whale.data.model.Category;
import com.odianyun.search.whale.data.service.CategoryService;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.index.api.common.IndexFieldConstants;

public class NavCategoryResponseHandler implements ResponseHandler<BaseSearchRequest> {

	@Autowired
	CategoryService categoryService;

	@Override
	public void handle(org.elasticsearch.action.search.SearchResponse esSearchResponse,
					   com.odianyun.search.whale.api.model.SearchResponse searchResponse,
					   ESSearchRequest esSearchRequest, BaseSearchRequest searchRequest) throws Exception {

		int companyId=searchResponse.getCompanyId();
		Map<String, Aggregation> aggregationMap = AggregationResponsePreprocess.process(esSearchResponse,searchResponse,esSearchRequest,searchRequest);
		InternalTerms internalTerms=(InternalTerms) aggregationMap.get(IndexFieldConstants.NAVCATEGORYID_SEARCH);

		if (internalTerms != null) {
			/*生成TreeResult的树状结构*/
			Map<Long, CategoryTreeResult> cateTreeResultMap = new HashMap<Long, CategoryTreeResult>();
			List<Terms.Bucket> counts=internalTerms.getBuckets();
	    	for(Terms.Bucket c:counts){
	    		long categoryId = Long.valueOf(c.getKey());
				long count = c.getDocCount();
				calcCategoryTree(categoryId, count, cateTreeResultMap,companyId);
	    	}
			/* 添加TreeResult的层级关系 */
			if (!cateTreeResultMap.isEmpty()) {
				Set<Long> rootSet = new HashSet<Long>();
				for (Entry<Long, CategoryTreeResult> entry : cateTreeResultMap
						.entrySet()) {
					Long parentCategoryId = categoryService.getParentCategoryId(entry.getKey(),companyId);
					if (parentCategoryId == null) {//循环到根节点
						rootSet.add(entry.getKey());
					} else {
						CategoryTreeResult parentTreeResult = cateTreeResultMap.get(parentCategoryId);
						if (parentTreeResult != null) {
							parentTreeResult.addChild(entry.getValue());
						}
					}
				}
                /*只取根节点的TreeResult返回*/
				List<CategoryTreeResult> categoryTreeResultList = new ArrayList<CategoryTreeResult>();
				for (Long rootId : rootSet) {
					categoryTreeResultList.add(cateTreeResultMap.get(rootId));
				}
				Collections.sort(categoryTreeResultList, new Comparator<CategoryTreeResult>() {

					@Override
					public int compare(CategoryTreeResult o1, CategoryTreeResult o2) {
						if(o2.getCount()>o1.getCount()){
							return 1;
						}else if(o2.getCount()<o1.getCount()){
							return -1;
						}else{
							return 0;
						}
					}
					
				} );
				searchResponse.setNavCategoryTreeResult(categoryTreeResultList);
			}
		}

	}

	/**
	 * 生成类目树，并计算父类的结果个数
	 * @param categoryId
	 * @param count
	 * @param cateTreeResultMap
	 * @throws Exception
	 */
	public void calcCategoryTree(long categoryId, long count,
			Map<Long, CategoryTreeResult> cateTreeResultMap,int companyId) throws Exception {
		Category category = categoryService.getCategory(categoryId,companyId);
		if(category == null){
			return;
		}
		CategoryTreeResult ctreeRet = new CategoryTreeResult(category.getId(),
				category.getName(), count);
		cateTreeResultMap.put(categoryId, ctreeRet);
		
	}

}
