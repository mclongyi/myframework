package com.odianyun.search.whale.resp.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.odianyun.search.whale.api.model.req.BaseSearchRequest;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import com.odianyun.search.whale.api.model.MerchantProduct;
import com.odianyun.search.whale.api.model.SearchResponse;
import com.odianyun.search.whale.common.MerchantProductConvertor;
import com.odianyun.search.whale.es.request.ESSearchRequest;

public class FieldsResponseHandler implements ResponseHandler{
	
	
	@Autowired
	MerchantProductConvertor merchantProductConvertor;

	public void handle(org.elasticsearch.action.search.SearchResponse esSearchResponse,
					   com.odianyun.search.whale.api.model.SearchResponse searchResponse,
					   ESSearchRequest esSearchRequest, BaseSearchRequest searchRequest) throws Exception {	    List<Long> merchantProductIds=new ArrayList<Long>();
	    searchResponse.setMerchantProductIds(merchantProductIds);  
	    SearchHits searchHits=esSearchResponse.getHits();
	    searchResponse.setTotalHit(searchHits.getTotalHits());
	    SearchHit[] searchHitArray=searchHits.getHits();
	    for(SearchHit hit:searchHitArray){
	    	merchantProductIds.add(Long.valueOf(hit.getId()));
	    	Map<String, SearchHitField> data = hit.fields();
	    	MerchantProduct merchantProduct = merchantProductConvertor.convertFromSearchHitField(data);
	    	searchResponse.getMerchantProductResult().add(merchantProduct);
	    }
	    
	}

}
