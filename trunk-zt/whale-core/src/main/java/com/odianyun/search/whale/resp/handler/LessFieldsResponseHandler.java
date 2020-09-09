package com.odianyun.search.whale.resp.handler;

import com.odianyun.search.whale.api.model.MerchantProduct;
import com.odianyun.search.whale.api.model.ZeroResultRecommendResult;
import com.odianyun.search.whale.api.model.req.BaseSearchRequest;
import com.odianyun.search.whale.common.MerchantProductConvertor;
import com.odianyun.search.whale.es.request.ESSearchRequest;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LessFieldsResponseHandler implements ResponseHandler<BaseSearchRequest> {


	@Autowired
	MerchantProductConvertor merchantProductConvertor;

	public void handle(org.elasticsearch.action.search.SearchResponse esSearchResponse,
					   com.odianyun.search.whale.api.model.SearchResponse searchResponse,
					   ESSearchRequest esSearchRequest, BaseSearchRequest searchRequest) throws Exception{
		SearchHit[] SearchHits = esSearchResponse.getHits().getHits();
		List<MerchantProduct> merchantProductResult = new ArrayList<>();

		for(SearchHit hit:SearchHits){
			Map<String, SearchHitField> data = hit.fields();
			MerchantProduct merchantProduct = merchantProductConvertor.convertFromSearchHitField(data);
			merchantProductResult.add(merchantProduct);
		}
		if(esSearchResponse.getHits().getTotalHits() > 0){
			searchResponse.setZeroResultRecommendResult(new ZeroResultRecommendResult(searchRequest.getKeyword(),merchantProductResult));
		}
	}


}