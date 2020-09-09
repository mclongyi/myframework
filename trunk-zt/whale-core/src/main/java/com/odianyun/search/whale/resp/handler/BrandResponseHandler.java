package com.odianyun.search.whale.resp.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.odianyun.search.whale.api.model.KeywordResult;
import com.odianyun.search.whale.api.model.req.BaseSearchRequest;

import com.odianyun.search.whale.data.service.ConfigService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.InternalAggregation;
import org.elasticsearch.search.aggregations.bucket.terms.InternalTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.api.model.BrandResult;
import com.odianyun.search.whale.data.model.Brand;
import com.odianyun.search.whale.data.service.BrandService;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.index.api.common.IndexFieldConstants;

public class BrandResponseHandler implements ResponseHandler<BaseSearchRequest> {
	
	@Autowired
	BrandService brandService;

	@Autowired
	ConfigService configService;
	
	@Override
	public void handle(org.elasticsearch.action.search.SearchResponse esSearchResponse,
					   com.odianyun.search.whale.api.model.SearchResponse searchResponse,
					   ESSearchRequest esSearchRequest, BaseSearchRequest searchRequest) throws Exception {
		int companyId=searchResponse.getCompanyId();
		String keyword=searchRequest.getKeyword();
		boolean openJumpBrandPage=configService.getBool("open_jump_brand_page",false,companyId);
		if(StringUtils.isNotBlank(keyword) && openJumpBrandPage && esSearchResponse.getHits().getTotalHits()>0){
			Brand brand=brandService.getBrand(keyword.trim().toLowerCase(),companyId);
			if(brand!=null){
				KeywordResult keywordResult=searchResponse.getKeywordResult();
				keywordResult.setKeyword(keyword);
				keywordResult.setBrandId(brand.getId());
			}
		}
		Map<String, Aggregation> aggregationMap= AggregationResponsePreprocess.process(esSearchResponse,searchResponse,esSearchRequest,searchRequest);
		InternalTerms internalTerms=(InternalTerms) aggregationMap.get(IndexFieldConstants.BRANDID_SEARCH);

		if (internalTerms != null) {
			Map<Long,BrandResult> brandResultMap = new HashMap<Long,BrandResult>();
			
			for(Terms.Bucket c:internalTerms.getBuckets()){
				Long valueId = Long.valueOf(c.getKey());
				Long count = c.getDocCount();
				BrandResult brandResult = new BrandResult();
				brandResult.setId(valueId);
				brandResult.setCount(count);
				brandResultMap.put(valueId, brandResult);
			}
			List<Long> brandIds = new ArrayList<Long>(brandResultMap.keySet());
			Map<Long,Brand> brands = brandService.getBrandMap(brandIds,companyId);
			for(Map.Entry<Long,BrandResult> entry : brandResultMap.entrySet()){
				BrandResult brandResult = entry.getValue();
				Brand brand = brands.get(entry.getKey());
				if(null != brand){
					String brandNmae = brand.getChinese_name();
					String brandLogo = brand.getBrandLogo();
					brandResult.setName(brandNmae);
					brandResult.setLogo(brandLogo);
				}
				
			}
			
			List<BrandResult> brandResultList = new ArrayList<BrandResult>(brandResultMap.values());
			Collections.sort(brandResultList, new Comparator<BrandResult>(){

				@Override
				public int compare(BrandResult o1, BrandResult o2) {
					if(o2.getCount()>o1.getCount()){
						return 1;
					}else if(o2.getCount()<o1.getCount()){
						return -1;
					}else{
						return 0;
					}
				}});
			
			searchResponse.setBrandResult(brandResultList);
		}
	}

}
