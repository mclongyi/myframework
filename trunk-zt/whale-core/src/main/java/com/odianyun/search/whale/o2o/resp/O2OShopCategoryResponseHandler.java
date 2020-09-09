package com.odianyun.search.whale.o2o.resp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.bucket.terms.InternalTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.MerchantProduct;
import com.odianyun.search.whale.api.model.o2o.O2OShopCategoryResponse;
import com.odianyun.search.whale.api.model.o2o.O2OShopSearchResponse;
import com.odianyun.search.whale.common.MerchantProductConvertor;
import com.odianyun.search.whale.data.model.Category;
import com.odianyun.search.whale.data.service.CategoryService;
import com.odianyun.search.whale.index.api.common.IndexFieldConstants;

public class O2OShopCategoryResponseHandler implements O2OShopSearchResponseHandler{
	
	static Logger logger = Logger.getLogger(O2OShopCategoryResponseHandler.class);
	
	@Autowired
	MerchantProductConvertor merchantProductConvertor;
	
	@Autowired
	CategoryService categoryService;

	@Override
	public void handle(SearchResponse searchResponse,
			O2OShopSearchResponse o2oShopSearchResponse) throws SearchException {
		try{
			int companyId=o2oShopSearchResponse.getCompanyId();
			InternalTerms internalTerms=(InternalTerms) searchResponse.getAggregations().asMap().get(IndexFieldConstants.MERCHANT_CATEGORYID);
			Map<Long,O2OShopCategoryResponse> categoryResponses=new HashMap<Long,O2OShopCategoryResponse>();
			if (internalTerms != null) {
				List<Terms.Bucket> counts=internalTerms.getBuckets();				
		    	for(Terms.Bucket c:counts){
		    		Long categoryId = Long.valueOf(c.getKey());
		    		if(!categoryResponses.containsKey(categoryId)){
		    			Category category=categoryService.getCategory(categoryId,companyId); 			
		    			if(category!=null){
		    				O2OShopCategoryResponse o2OShopCategoryResponse=new O2OShopCategoryResponse();
		    				o2OShopCategoryResponse.setMerchantCategoryId(categoryId);
		    				o2OShopCategoryResponse.setMerchantCategoryName(category.getName());
		    				categoryResponses.put(categoryId, o2OShopCategoryResponse);
		    				o2oShopSearchResponse.getShopCategoryResponse().add(o2OShopCategoryResponse);
		    			}		    			
		    		}
		    	}
			}
			SearchHits searchHits=searchResponse.getHits();
		    SearchHit[] searchHitArray=searchHits.getHits();
		    O2OShopCategoryResponse noFoundCategory=new O2OShopCategoryResponse();
		    for(SearchHit hit:searchHitArray){
		    	Map<String, SearchHitField> data = hit.fields();
		    	MerchantProduct merchantProduct =merchantProductConvertor.convertFromSearchHitField(data);
		    	O2OShopCategoryResponse o2OShopCategoryResponse=categoryResponses.get(merchantProduct.getCategoryId());
		    	if(o2OShopCategoryResponse!=null){
		    		o2OShopCategoryResponse.getMerchantProducts().add(merchantProduct);
		    	}else{
		    		noFoundCategory.getMerchantProducts().add(merchantProduct);
		    	}
		    }
		    if(CollectionUtils.isNotEmpty(noFoundCategory.getMerchantProducts())){
		    	o2oShopSearchResponse.getShopCategoryResponse().add(noFoundCategory);
		    }
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new SearchException(e.getMessage(), e);
		}
		
	}

}
