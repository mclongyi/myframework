package com.odianyun.search.whale.resp.handler;

import com.odianyun.search.whale.api.model.req.BaseSearchRequest;

import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.bucket.terms.InternalTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.api.model.AttributeResult;
import com.odianyun.search.whale.api.model.AttributeValueResult;
import com.odianyun.search.whale.api.model.SearchResponse;
import com.odianyun.search.whale.data.model.AttributeValue;
import com.odianyun.search.whale.data.service.AttributeValueService;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.index.api.common.IndexFieldConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AttributeResponseHandler implements ResponseHandler{
	
	@Autowired
	AttributeValueService attributeValueService;

	public void handle(org.elasticsearch.action.search.SearchResponse esSearchResponse,
					   com.odianyun.search.whale.api.model.SearchResponse searchResponse,
					   ESSearchRequest esSearchRequest, BaseSearchRequest searchRequest){
		int companyId=searchResponse.getCompanyId();
		Map<String, Aggregation> aggregationMap = AggregationResponsePreprocess.process(esSearchResponse,searchResponse,esSearchRequest,searchRequest);
		InternalTerms internalTerms=(InternalTerms) aggregationMap.get(IndexFieldConstants.ATTRVALUEID_SEARCH);

		if(internalTerms!=null){
			Map<Long,AttributeResult> attributeResultMap=new HashMap<Long,AttributeResult>();
			for(Terms.Bucket c:internalTerms.getBuckets()){
				Long valueId=Long.valueOf(c.getKey());
				Long count=c.getDocCount();
				if(attributeValueService.isAttributeValueGroup(valueId,companyId)){
					continue;
				}
				AttributeValue attributeValue=attributeValueService.getAttributeValue(valueId,companyId);
				if(attributeValue!=null){
					AttributeValueResult attributeValueResult=new AttributeValueResult(attributeValue.getAttrValueId(),
							attributeValue.getAttrValue(),attributeValue.getAttrNameId(),
							count,attributeValue.getSortValue()==null?999:attributeValue.getSortValue());
					AttributeResult attributeResult=attributeResultMap.get(attributeValue.getAttrNameId());
					if(attributeResult==null){
						attributeResult=new AttributeResult(attributeValue.getAttrNameId(),attributeValue.getAttrName());
						attributeResultMap.put(attributeValue.getAttrNameId(), attributeResult);
					}
					attributeResult.setCount(attributeResult.getCount()+count);
					attributeResult.getAttributeValues().add(attributeValueResult);
				}
				
			}
			List<AttributeResult> attributeResultList=new ArrayList<AttributeResult>(attributeResultMap.values());
			Collections.sort(attributeResultList, new Comparator<AttributeResult>() {

				@Override
				public int compare(AttributeResult o1, AttributeResult o2) {
					if(o2.getCount()>o1.getCount()){
						return 1;
					}else if(o2.getCount()<o1.getCount()){
						return -1;
					}else{
						return 0;
					}
				}
				
			} );
			for(AttributeResult attributeResult:attributeResultList){
				Collections.sort(attributeResult.getAttributeValues(), new Comparator<AttributeValueResult>() {
					@Override
					public int compare(AttributeValueResult o1, AttributeValueResult o2) {
						if(o1.getSortValue()>o2.getSortValue()){
							return 1;
						}else if(o1.getSortValue()<o2.getSortValue()){
							return -1;
						}else{
							return 0;
						}
					}
					
				} );
			}
			searchResponse.setAttributeResult(attributeResultList);
		}
		
		
	}

}
