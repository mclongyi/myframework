package com.odianyun.search.whale.req.builder;

import com.odianyun.search.whale.api.model.req.BaseSearchRequest;
import com.odianyun.search.whale.es.request.ESSearchRequest;

import org.elasticsearch.index.query.QueryBuilder;

/**
 * Created by admin on 2016/6/2.
 */
public interface RequestScoreBuilder {
    void alterScoreBySingleTokenForCate(ESSearchRequest esSearchRequest, BaseSearchRequest searchRequest);
}
