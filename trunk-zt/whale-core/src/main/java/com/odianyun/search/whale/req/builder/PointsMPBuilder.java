package com.odianyun.search.whale.req.builder;

import com.odianyun.search.whale.api.model.PointNumRange;
import com.odianyun.search.whale.api.model.req.BaseSearchRequest;
import com.odianyun.search.whale.api.model.req.PointSearchRequest;
import com.odianyun.search.whale.api.model.req.PointType;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.index.api.common.IndexFieldConstants;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.index.query.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fishcus on 17/8/22.
 */
public class PointsMPBuilder implements RequestBuilder{

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

    @Override
    public void build(ESSearchRequest esSearchRequest, BaseSearchRequest searchRequest) {
        if(searchRequest.getRequestType() != IndexConstants.POINTS_SEARCH){
            return;
        }
        filterBuild(esSearchRequest,searchRequest);
        fieldsBuild(esSearchRequest,searchRequest);
        queryBuild(esSearchRequest,searchRequest);
    }

    private void queryBuild(ESSearchRequest esSearchRequest, BaseSearchRequest searchRequest) {
        PointSearchRequest pointSearchRequest = (PointSearchRequest)searchRequest;

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        PointNumRange pointRange = pointSearchRequest.getPointNumRange();
        if(pointRange != null){
            RangeQueryBuilder rangeQueryBuilder = new RangeQueryBuilder(IndexFieldConstants.POINTS_PRICE);
            if(pointRange.getMinPoint()!=null){
                rangeQueryBuilder.from(pointRange.getMinPoint());
            }
            if(pointRange.getMaxPoint()!=null){
                rangeQueryBuilder.to(pointRange.getMaxPoint());
            }
            boolQueryBuilder.must(rangeQueryBuilder);
        }

        Date now = new Date();
        RangeQueryBuilder endTimeQueryBuilder = new RangeQueryBuilder(IndexFieldConstants.POINT_START_TIME);
        endTimeQueryBuilder.to(simpleDateFormat.format(now));
        boolQueryBuilder.must(endTimeQueryBuilder);
        RangeQueryBuilder startTimeQueryBuilder = new RangeQueryBuilder(IndexFieldConstants.POINT_END_TIME);
        startTimeQueryBuilder.from(simpleDateFormat.format(now));
        boolQueryBuilder.must(startTimeQueryBuilder);

        if(boolQueryBuilder.hasClauses()){
            QueryBuilder queryBuilder = esSearchRequest.getQueryBuilder();
            if(queryBuilder != null){
                boolQueryBuilder.must(queryBuilder);
            }
            esSearchRequest.setQueryBuilder(boolQueryBuilder);
        }
    }

    private void fieldsBuild(ESSearchRequest esSearchRequest, BaseSearchRequest searchRequest) {
        List<String> fields = esSearchRequest.getFields();
        fields.add(IndexFieldConstants.POINTS_PRICE);
        fields.add(IndexFieldConstants.POINTS_PRICE_TYPE);
        fields.add(IndexFieldConstants.POINTS_POINT_TYPE);
        esSearchRequest.setFields(fields);
    }

    private void filterBuild(ESSearchRequest esSearchRequest, BaseSearchRequest searchRequest) {
        PointSearchRequest pointSearchRequest = (PointSearchRequest)searchRequest;
        List<PointType> pointTypeList = pointSearchRequest.getPointTypeList();
        if(CollectionUtils.isNotEmpty(pointTypeList)){
            BoolFilterBuilder boolFilter = new BoolFilterBuilder();

            org.elasticsearch.index.query.FilterBuilder[] filterBuilders =
                    new org.elasticsearch.index.query.FilterBuilder[pointTypeList
                            .size()];
            for(int i = 0; i < pointTypeList.size(); i++){
                PointType pointType = pointTypeList.get(i);
                String fieldName = null;
                String fieldValue = null;
                if(pointType == PointType.POINT_AND_MON){
                    fieldName = IndexFieldConstants.POINT_PRICE_TYPE;
                    fieldValue = "1";
                }else if(pointType == PointType.POINT_ONLY){
                    fieldName = IndexFieldConstants.POINT_PRICE_TYPE;
                    fieldValue = "0";
                }else if(pointType == PointType.REF_MP){
                    fieldName = IndexFieldConstants.POINT_REF_TYPE;
                    fieldValue = "0";
                }else if(pointType == PointType.REF_VOUCHER){
                    fieldName = IndexFieldConstants.POINT_REF_TYPE;
                    fieldValue = "1";
                }
                filterBuilders[i] = new TermFilterBuilder(fieldName, fieldValue);

            }
            boolFilter.must(filterBuilders);
            boolFilter.must(new TermFilterBuilder(IndexFieldConstants.IS_DEFAULT, 1));
            if(boolFilter.hasClauses()){
                org.elasticsearch.index.query.FilterBuilder filterBuilder = esSearchRequest.getFilterBuilder();
                if(filterBuilder != null){
                    boolFilter.must(filterBuilder);
                }
                esSearchRequest.setFilterBuilder(boolFilter);
            }

        }

    }

}
