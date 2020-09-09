package com.odianyun.search.whale.recommend.server;

import com.odianyun.architecture.caddy.SystemContext;
import com.odianyun.search.whale.api.model.SearchResponse;
import com.odianyun.search.whale.api.model.req.HotSearchRequest;
import com.odianyun.search.whale.api.model.req.SearchRequest;
import com.odianyun.search.whale.api.model.req.SortType;
import com.odianyun.search.whale.api.model.resp.HotSearchResponse;
import com.odianyun.search.whale.api.recommend.RecommendRequest;
import com.odianyun.search.whale.api.recommend.RecommendResponse;
import com.odianyun.search.whale.api.recommend.RecommendService;
import com.odianyun.search.whale.server.HotSearchHandler;
import com.odianyun.search.whale.server.SearchHandler;
import com.odianyun.search.whale.server.WhaleServer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zengfenghua on 16/12/12.
 */
public class HomeGuessHandler implements RecommendService{

    @Autowired
    HotSearchHandler hotSearchHandler;

    @Autowired
    WhaleServer whaleServer;

    @Override
    public RecommendResponse recommend(RecommendRequest recommendRequest) throws Exception {
        Long companyId= SystemContext.getCompanyId();
        SearchRequest searchRequest=new SearchRequest(companyId.intValue());
        searchRequest.setStart(recommendRequest.getStart());
        searchRequest.setCount(recommendRequest.getCount());
        searchRequest.setKeyword("*****");
        searchRequest.setMerchantIdList(recommendRequest.getMerchantIds());
        searchRequest.setSaleAreaCode(recommendRequest.getSaleAreaCodes());
        List<SortType> sortTypeList = new ArrayList<SortType>();
        sortTypeList.add(SortType.volume4sale_desc);
        searchRequest.setSortTypeList(sortTypeList);
        SearchResponse searchResponse=whaleServer.search(searchRequest);
        RecommendResponse recommendResponse=new RecommendResponse();
        recommendResponse.setMerchantProducts(searchResponse.getMerchantProductResult());
        return recommendResponse;
    }
}
