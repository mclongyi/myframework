package com.odianyun.search.whale.common.query;

import com.odianyun.search.whale.api.model.ManagementType;
import com.odianyun.search.whale.common.IndexNameManager;
import com.odianyun.search.whale.es.api.ESService;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.index.api.common.IndexFieldConstants;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;

/**
 * Created by zengfenghua on 16/9/19.
 */
public class KeywordSearchHelper {


    public static long count(String keyword,int companyId){
        long count=0;
        if(StringUtils.isBlank(keyword)){
            return count;
        }
        ESSearchRequest esSearchRequest=new ESSearchRequest(IndexNameManager.getIndexName(), IndexConstants.index_type);
        BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();
        boolQueryBuilder.must(new TermQueryBuilder(IndexFieldConstants.COMPANYID,companyId));
        boolQueryBuilder.must(KeywordQueryBuilder.buildKeywordQuery(keyword));
        boolQueryBuilder.must(new TermQueryBuilder(IndexFieldConstants.MANAGEMENT_STATE, ManagementType.ON_SHELF.getCode()));
        esSearchRequest.setQueryBuilder(boolQueryBuilder);
        try{
            CountResponse countResponse=ESService.count(esSearchRequest);
            count=countResponse.getCount();
        }catch (Exception e){

        }
        return count;
    }
}
