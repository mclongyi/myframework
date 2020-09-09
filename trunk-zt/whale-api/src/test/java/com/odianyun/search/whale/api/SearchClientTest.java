package com.odianyun.search.whale.api;

import com.odianyun.cc.client.spring.OccPropertiesLoaderUtils;
import com.odianyun.search.whale.api.model.SearchResponse;
import com.odianyun.search.whale.api.model.req.SearchRequest;
import com.odianyun.search.whale.api.service.SearchClient;
import com.odianyun.search.whale.api.service.SearchService;

/**
 * Created by zengfenghua on 16/9/23.
 */
public class SearchClientTest {

    public static void main(String[] args){
        System.setProperty("global.config.path","/Users/fishcus/JavaDev/data/env-199");
        OccPropertiesLoaderUtils.getProperties("osoa");
        SearchService searchService= SearchClient.getSearchService("local-test");
        SearchRequest searchRequest=new SearchRequest(1001);
        searchRequest.setKeyword("1");
        SearchResponse searchResponse=searchService.search(searchRequest);
        System.out.println(searchResponse.getMerchantProductResult());
    }
}
