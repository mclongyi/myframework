package com.odianyun.search.whale.resp.handler;

import com.odianyun.search.whale.api.model.req.BaseSearchRequest;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.index.api.common.IndexFieldConstants;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.InternalAggregation;
import org.elasticsearch.search.aggregations.InternalAggregations;
import org.elasticsearch.search.aggregations.bucket.filter.InternalFilter;
import org.elasticsearch.search.aggregations.bucket.terms.InternalTerms;
import org.elasticsearch.search.aggregations.bucket.global.InternalGlobal;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zengfenghua on 16/9/13.
 */
public class AggregationResponsePreprocess {

    private static Map<String, Aggregation> EMPTY_MAP = new HashMap<String, Aggregation>();

    public static Map<String, Aggregation> process(SearchResponse esSearchResponse, com.odianyun.search.whale.api.model.SearchResponse searchResponse,
                       ESSearchRequest esSearchRequest, BaseSearchRequest searchRequest) {
        if(!searchRequest.isAggregation()){
            return EMPTY_MAP;
        }
        Map<String, Aggregation> aggregationMap=esSearchResponse.getAggregations().asMap();
        if(CollectionUtils.isNotEmpty(esSearchRequest.getFacet_fields()) && esSearchRequest.getAggregationQueryBuilder()!=null){
            InternalGlobal internalAggregations=(InternalGlobal) aggregationMap.get("global");
            if(internalAggregations!=null){
                InternalFilter filterInternalAggregations=(InternalFilter)internalAggregations.getAggregations().asMap().get("filter");
                if(filterInternalAggregations!=null){
                   return filterInternalAggregations.getAggregations().asMap();
                }
            }
        }
        return aggregationMap;

    }
}
