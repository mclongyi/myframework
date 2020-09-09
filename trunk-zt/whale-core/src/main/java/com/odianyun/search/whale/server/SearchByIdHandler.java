package com.odianyun.search.whale.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.odianyun.search.whale.common.IndexNameManager;
import com.odianyun.search.whale.common.QueryByIdBuilder;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.api.model.ManagementType;
import com.odianyun.search.whale.api.model.MerchantProduct;
import com.odianyun.search.whale.common.MerchantProductConvertor;
import com.odianyun.search.whale.common.RequestFieldsBuilder;
import com.odianyun.search.whale.es.api.ESService;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.index.api.common.IndexFieldConstants;

public class SearchByIdHandler {

	static Logger logger = Logger.getLogger(SearchByIdHandler.class);

	@Autowired
	MerchantProductConvertor merchantProductConvertor;


	public Map<Long, MerchantProduct> searchById(List<Long> mpIds, Integer companyId) {
		Map<Long, MerchantProduct> ret = new HashMap<Long, MerchantProduct>();
		try {
			ESSearchRequest esSearchRequest = new ESSearchRequest(IndexNameManager.getIndexName(),
					IndexConstants.index_type);

			QueryByIdBuilder.build(esSearchRequest,mpIds,companyId,0,Integer.MAX_VALUE);

			SearchResponse searchResponse = ESService.search(esSearchRequest);
			SearchHits searchHits = searchResponse.getHits();
			SearchHit[] searchHitArray = searchHits.getHits();
			for (SearchHit hit : searchHitArray) {
				Map<String, SearchHitField> data = hit.fields();
				MerchantProduct merchantProduct = merchantProductConvertor.convertFromSearchHitField(data);
				ret.put(merchantProduct.getId(), merchantProduct);
			}

		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return ret;
	}

	public Map<String, MerchantProduct> searchByMpCodes(List<String> mpCodes, Integer companyId) {
		Map<String, MerchantProduct> ret = new HashMap<String, MerchantProduct>();
		try {
			BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
			TermQueryBuilder compandyIdQuery = new TermQueryBuilder(IndexFieldConstants.COMPANYID, companyId);
			boolQueryBuilder.must(compandyIdQuery);
			BoolQueryBuilder mpCodesBoolQueryBuilder = new BoolQueryBuilder();
			boolQueryBuilder.must(mpCodesBoolQueryBuilder);
			for (String mpCode : mpCodes) {
				TermQueryBuilder mpCodeQuery = new TermQueryBuilder(IndexFieldConstants.CODE, mpCode);
				mpCodesBoolQueryBuilder.should(mpCodeQuery);
			}
			boolQueryBuilder.must(new TermQueryBuilder(IndexFieldConstants.MANAGEMENT_STATE, ManagementType.ON_SHELF.getCode()));

			ESSearchRequest esSearchRequest = new ESSearchRequest(IndexNameManager.getIndexName(),
					IndexConstants.index_type);
			esSearchRequest.setQueryBuilder(boolQueryBuilder);
			esSearchRequest.setStart(0);
			esSearchRequest.setCount(Integer.MAX_VALUE);
			RequestFieldsBuilder.build(esSearchRequest);

			SearchResponse searchResponse = ESService.search(esSearchRequest);
			SearchHits searchHits = searchResponse.getHits();
			SearchHit[] searchHitArray = searchHits.getHits();
			for (SearchHit hit : searchHitArray) {
				Map<String, SearchHitField> data = hit.fields();
				MerchantProduct merchantProduct = merchantProductConvertor.convertFromSearchHitField(data);
				MerchantProduct merchantProduct_pre=ret.get(merchantProduct.getCode());
				if(merchantProduct_pre==null || (merchantProduct.getTypeOfProduct()!=null && merchantProduct.getTypeOfProduct()==3)){
					ret.put(merchantProduct.getCode(), merchantProduct);
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return ret;
	}

}
