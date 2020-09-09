package com.odianyun.search.whale.selectionproduct;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.MerchantProduct;
import com.odianyun.search.whale.api.model.selectionproduct.SelectionMerchantProductSearchResponse;
import com.odianyun.search.whale.api.model.selectionproduct.SelectionProduct;
import com.odianyun.search.whale.api.model.selectionproduct.SelectionProductSearchResponse;
import com.odianyun.search.whale.common.MerchantProductConvertor;
import com.odianyun.search.whale.data.model.Category;
import com.odianyun.search.whale.data.service.CategoryService;

public class SelectionProductSearchResponseHandler {
	
	static Logger logger = Logger.getLogger(SelectionProductSearchResponseHandler.class);
	
	@Autowired
	MerchantProductConvertor merchantProductConvertor;
	
	@Autowired
	CategoryService categoryService;
	
	public SelectionProductSearchResponse handle(SearchResponse searchResponse){
		SelectionProductSearchResponse selectionProductSearchResponse=new SelectionProductSearchResponse();
		try{
			SearchHits searchHits=searchResponse.getHits();
		    SearchHit[] searchHitArray=searchHits.getHits();
		    Map<Long,SelectionProduct> selectionProducts=new HashMap<Long, SelectionProduct>();
		    for(SearchHit hit:searchHitArray){
		    	Map<String, SearchHitField> data = hit.fields();
		    	MerchantProduct merchantProduct =merchantProductConvertor.convertFromSearchHitField(data);
		    	Long productId=merchantProduct.getProductId();
		    	if(productId!=null&&productId!=0){
		    		SelectionProduct selectionProduct=selectionProducts.get(productId);
		    		if(selectionProduct==null){
		    			selectionProduct=new SelectionProduct();
		    			selectionProduct.setId(productId);
		    			selectionProduct.setName(merchantProduct.getProductName());
		    			selectionProduct.setCategoryId(merchantProduct.getCategoryId());
		    			if(merchantProduct.getCategoryId()!=null){
		    				Category category=categoryService.getCategory(merchantProduct.getCategoryId(),merchantProduct.getCompanyId().intValue()); 			
			    			if(category!=null){
			    				selectionProduct.setCategoryName(category.getName());
			    			}
		    			}
		    			selectionProducts.put(productId, selectionProduct);
		    			selectionProductSearchResponse.getSelectionProducts().add(selectionProduct);
		    		}
		    		selectionProduct.getMerchantProducts().add(merchantProduct);
		    	}
		    	
		    }
		}catch(SearchException e){
			throw e;
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new SearchException(e.getMessage(), e);
		}
	    return selectionProductSearchResponse;
	}
	
	
	public SelectionMerchantProductSearchResponse handle2(SearchResponse searchResponse){
		SelectionMerchantProductSearchResponse selectionProductSearchResponse=new SelectionMerchantProductSearchResponse();
		try{
			SearchHits searchHits=searchResponse.getHits();
		    SearchHit[] searchHitArray=searchHits.getHits();
		    selectionProductSearchResponse.setTotalHit(searchHits.getTotalHits());
		    for(SearchHit hit:searchHitArray){
		    	Map<String, SearchHitField> data = hit.fields();
		    	MerchantProduct merchantProduct =merchantProductConvertor.convertFromSearchHitField(data);
		    	if(merchantProduct.getCategoryId()!=null){
    				Category category=categoryService.getCategory(merchantProduct.getCategoryId(),merchantProduct.getCompanyId().intValue()); 			
	    			if(category!=null){
	    				merchantProduct.setCategoryName(category.getName());
	    			}
    			}
		    	selectionProductSearchResponse.getMerchantProducts().add(merchantProduct);
		    }
		}catch(SearchException e){
			throw e;
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new SearchException(e.getMessage(), e);
		}
	    return selectionProductSearchResponse;
	}

}
