package com.odianyun.search.whale.server.cache;

import com.odianyun.search.whale.api.model.SearchResponse;
import com.odianyun.search.whale.api.model.req.SearchRequest;
import com.odianyun.search.whale.cache.SearchCacheImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zengfenghua on 16/10/26.
 */
public class SearchCacheImplTest {

    public static void main(String[] args){
//        System.setProperty("global.config.path", "/data/ds_dev_env");
        System.setProperty("global.config.path", "/svn/odianyun/prop/lyf-branch");
//        System.setProperty("global.config.path", "C:\\svn\\odianyun\\prop\\lyf-branch");
        SearchRequest searchRequest=new SearchRequest(10);
//        searchRequest.setKeyword("女装");
        List<String> eans= new ArrayList<String>();
        eans.add("kk");
        eans.add("ggg");
        searchRequest.setEanNos(eans);
        searchRequest.setKeyword("kk");
        SearchResponse searchResponse=new SearchResponse();
        searchResponse.setTotalHit(400l);
        SearchCacheImpl.instance.put(searchRequest,searchResponse);
        searchResponse= SearchCacheImpl.instance.get(searchRequest);
        System.out.println(searchResponse);
    }


}
