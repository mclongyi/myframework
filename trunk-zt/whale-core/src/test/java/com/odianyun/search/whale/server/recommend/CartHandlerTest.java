package com.odianyun.search.whale.server.recommend;

import com.odianyun.architecture.caddy.SystemContext;
import com.odianyun.search.whale.api.recommend.*;
import com.odianyun.search.whale.server.AbstractTest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zengfenghua on 16/12/12.
 */
public class CartHandlerTest extends AbstractTest {

    public static void  main(String[] args)throws Exception{
        System.setProperty("global.config.path", "/data/lyf_dev_env");
        SystemContext.setCompanyId(30l);
        RecommendService recommendService= RecommendClient.getRecommendService("test");
        // RecommendService recommendService=HessionServiceFactory.getService
        //    ("http://192.168.8.17:8080/search/soa/SOARecommendService",RecommendService.class);
        RecommendRequest recommendRequest=new RecommendRequest(RecommendScene.CART);
        List<Long> mpIds=new ArrayList<Long>();
        mpIds.add(1049011601005458l);
        recommendRequest.setCurrentVisitMpIds(mpIds);
        RecommendResponse recommendResponse=recommendService.recommend(recommendRequest);
        System.out.println(recommendResponse);
    }
}
