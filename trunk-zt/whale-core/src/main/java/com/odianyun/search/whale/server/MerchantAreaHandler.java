package com.odianyun.search.whale.server;

import com.odianyun.search.whale.api.model.req.BaseSearchRequest;
import com.odianyun.search.whale.api.model.resp.AreaResult;
import com.odianyun.search.whale.data.model.Area;
import com.odianyun.search.whale.data.model.SaleAreasCover;
import com.odianyun.search.whale.data.service.AreaService;
import com.odianyun.search.whale.es.api.ESService;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.index.api.common.IndexFieldConstants;
import com.odianyun.search.whale.index.api.common.MerchantAreaIndexContants;
import com.odianyun.search.whale.index.api.common.MerchantAreaIndexFieldContants;
import com.odianyun.search.whale.index.api.common.OplusOIndexConstants;
import com.odianyun.search.whale.resp.handler.AggregationResponsePreprocess;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.bucket.terms.InternalTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by Fu Yifan on 2017/11/27.
 */
public class MerchantAreaHandler {
    private static Logger logger = Logger.getLogger(MerchantAreaHandler.class);
    @Autowired
    AreaService areaService;

    static final int DEFAULT_MERCHANT_FLAG = 1;

    public List<AreaResult> getMerchantServiceAreaList(Long areaCode, Integer level, Long companyId) {
        List<AreaResult> areaResult = new ArrayList<>();
        if (level == null && areaCode == null) {
            level = 2;
        }
        try {
            com.odianyun.search.whale.api.model.SearchResponse response = new com.odianyun.search.whale.api.model.SearchResponse();
            BaseSearchRequest searchRequest = new BaseSearchRequest(companyId.intValue());
            ESSearchRequest esSearchRequest = new ESSearchRequest(OplusOIndexConstants.index_alias,
                    MerchantAreaIndexContants.index_type);
            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
            TermQueryBuilder termQuery = new TermQueryBuilder(IndexFieldConstants.COMPANYID,companyId);
            boolQueryBuilder.must(termQuery);
            esSearchRequest.setQueryBuilder(boolQueryBuilder);
            String[] facet_fields = new String[] { IndexFieldConstants.AREA_CODES};
            esSearchRequest.setFacet_fields(Arrays.asList(facet_fields));
            SearchResponse esSearchResponse = ESService.search(esSearchRequest);
            Map<String, Aggregation> aggregationMap= AggregationResponsePreprocess.process(esSearchResponse, response, esSearchRequest, searchRequest);
            InternalTerms longTerms=(InternalTerms) aggregationMap.get(IndexFieldConstants.AREA_CODES);
            if (longTerms != null) {
                List<Terms.Bucket> buckets=longTerms.getBuckets();
                List<Long> areaCodes = new ArrayList<>();
                List<AreaResult> tempList = new ArrayList<>();
                for(Terms.Bucket c:buckets){
                    Long areaCodeKey = Long.valueOf(c.getKey());
                    areaCodes.add(areaCodeKey);
                    AreaResult area = new AreaResult();
                    area.setAreaCode(areaCodeKey);
                    area.setCount(c.getDocCount());
                    tempList.add(area);
                }
                Map<Long, Area> areaMap = areaService.getAreaMap(areaCodes, companyId.intValue());
                for (AreaResult temp : tempList) {
                    Area area = areaMap.get(temp.getAreaCode());
                    if (area != null) {
                        if (level == null || (level != null && area.getLevel() != level)) {
                            continue;
                        }
                        temp.setAreaName(area.getName());
                        areaResult.add(temp);
                    }
                }
                Collections.sort(areaResult, new Comparator<AreaResult>() {
                    public int compare(AreaResult o1, AreaResult o2) {
                        return (int) (o2.getCount() - o1.getCount());
                    }
                });

            }
        } catch (Exception e) {
            logger.error("获取商家服务区域列表异常", e);
        }
        return areaResult;
    }
}
