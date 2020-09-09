package com.odianyun.search.whale.resp.handler;

import com.odianyun.search.whale.api.model.MerProScript;
import com.odianyun.search.whale.api.model.req.BaseSearchRequest;
import com.odianyun.search.whale.data.model.SuperScript;
import com.odianyun.search.whale.data.service.SuperScriptService;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/12/6.
 */
public class SuperScriptResponseHandler implements ResponseHandler {

    @Autowired
    private SuperScriptService superScriptService;

    @Override
    public void handle(SearchResponse esSearchResponse, com.odianyun.search.whale.api.model.SearchResponse searchResponse, ESSearchRequest esSearchRequest, BaseSearchRequest searchRequest) throws Exception {
        int companyId = searchResponse.getCompanyId();
        List<Long> mpIds = searchResponse.getMerchantProductIds();
        Map<Long,List<SuperScript>> scriptMap = superScriptService.queryMerPorScript(mpIds,companyId);
        if(scriptMap.size()>0){
            //searchResponse.setSuperScriptMap(scriptMap);
        }
    }
}
