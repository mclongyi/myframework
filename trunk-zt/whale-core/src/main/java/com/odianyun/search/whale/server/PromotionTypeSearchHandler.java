package com.odianyun.search.whale.server;

import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.ManagementType;
import com.odianyun.search.whale.api.model.req.PromotionTypeSearchRequest;
import com.odianyun.search.whale.api.model.resp.PromotionTypeSearchResponse;
import com.odianyun.search.whale.common.IndexNameManager;
import com.odianyun.search.whale.es.api.ESService;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.index.api.common.IndexFieldConstants;
import com.odianyun.search.whale.req.builder.CombineQueryBuilder;
import com.odianyun.search.whale.req.builder.SearchQueryStrBuilder;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.bucket.terms.InternalTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by fishcus on 16/11/13.
 */
public class PromotionTypeSearchHandler {

    static Logger logger = Logger.getLogger(PromotionTypeSearchHandler.class);

    public PromotionTypeSearchResponse handle(PromotionTypeSearchRequest searchRequest) throws SearchException {
        Date startTime = new Date();

        PromotionTypeSearchResponse response = new PromotionTypeSearchResponse();

        try {
            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
            boolQueryBuilder.must(new TermQueryBuilder(IndexFieldConstants.COMPANYID, searchRequest.getCompanyId()));
            boolQueryBuilder.must(new TermQueryBuilder(IndexFieldConstants.BRANDID_SEARCH,String.valueOf(searchRequest.getBrandId())));

            boolQueryBuilder.must(new TermQueryBuilder(IndexFieldConstants.MANAGEMENT_STATE, ManagementType.ON_SHELF.getCode()));

            ESSearchRequest esSearchRequest = new ESSearchRequest(IndexNameManager.getIndexName(),IndexConstants.index_type);
            boolQueryBuilder.must(CombineQueryBuilder.buildCombineQuery());
            esSearchRequest.setQueryBuilder(boolQueryBuilder);
            esSearchRequest.setStart(0);
            esSearchRequest.setCount(Integer.MAX_VALUE);
            List<String> facetFields = new ArrayList<>();
            facetFields.add(IndexFieldConstants.PROMOTIOM_TYPE_SEARCH);
            esSearchRequest.setFacet_fields(facetFields);

            SearchResponse esResponse = ESService.search(esSearchRequest);
            Map<String, Aggregation> aggregationMap = esResponse.getAggregations().asMap();
            InternalTerms internalTerms = (InternalTerms) aggregationMap.get(IndexFieldConstants.PROMOTIOM_TYPE_SEARCH);

            if(internalTerms != null){
                List<Terms.Bucket> bucckets = internalTerms.getBuckets();
                for(Terms.Bucket bucket : bucckets){
                    response.getPromotionTypeList().add(bucket.getKeyAsNumber().intValue());
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        Date endTime = new Date();
        response.setCostTime(endTime.getTime() - startTime.getTime());

        return response;
    }

}