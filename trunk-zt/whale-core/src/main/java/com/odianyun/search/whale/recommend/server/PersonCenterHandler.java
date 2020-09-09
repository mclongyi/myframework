package com.odianyun.search.whale.recommend.server;

import com.odianyun.architecture.caddy.SystemContext;
import com.odianyun.architecture.caddy.common.utils.RandomUtils;
import com.odianyun.search.whale.api.model.MerchantProduct;
import com.odianyun.search.whale.api.model.SearchResponse;
import com.odianyun.search.whale.api.model.req.SearchRequest;
import com.odianyun.search.whale.api.model.req.SortType;
import com.odianyun.search.whale.api.recommend.RecommendRequest;
import com.odianyun.search.whale.api.recommend.RecommendResponse;
import com.odianyun.search.whale.api.recommend.RecommendService;
import com.odianyun.search.whale.server.HotSearchHandler;
import com.odianyun.search.whale.server.WhaleServer;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by zengfenghua on 17/8/26.
 */
public class PersonCenterHandler implements RecommendService{

    @Autowired
    WhaleServer whaleServer;

    @Override
    public RecommendResponse recommend(RecommendRequest recommendRequest) throws Exception {
        Long companyId= SystemContext.getCompanyId();
        SearchRequest searchRequest=new SearchRequest(companyId.intValue());
        searchRequest.setStart(0);
        searchRequest.setCount(100);
        searchRequest.setKeyword("*****");
        searchRequest.setDistributionMp(recommendRequest.getDistributionMp());
        searchRequest.setMerchantIdList(recommendRequest.getMerchantIds());
        searchRequest.setSaleAreaCode(recommendRequest.getSaleAreaCodes());
        List<SortType> sortTypeList = new ArrayList<SortType>();
        sortTypeList.add(SortType.volume4sale_desc);
        searchRequest.setSortTypeList(sortTypeList);
        SearchResponse searchResponse=whaleServer.search(searchRequest);
        RecommendResponse recommendResponse=new RecommendResponse();
        List<MerchantProduct> merchantProductList=searchResponse.getMerchantProductResult();
        if(CollectionUtils.isNotEmpty(merchantProductList)){
            Collections.shuffle(merchantProductList);
            int size=merchantProductList.size();
            int to=recommendRequest.getCount();
            if(size<recommendRequest.getCount()){
                to=size;
            }
            merchantProductList=merchantProductList.subList(0,to);
        }
        recommendResponse.setMerchantProducts(merchantProductList);
        return recommendResponse;
    }
}
