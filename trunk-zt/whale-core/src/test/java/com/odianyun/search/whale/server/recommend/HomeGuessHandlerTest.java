package com.odianyun.search.whale.server.recommend;

import com.odianyun.architecture.caddy.SystemContext;
import com.odianyun.search.whale.api.recommend.*;
import com.odianyun.search.whale.api.service.HessionServiceFactory;
import com.odianyun.search.whale.server.AbstractTest;

/**
 * Created by zengfenghua on 16/12/12.
 */
public class HomeGuessHandlerTest extends AbstractTest{

    public static void main(String[] args) throws Exception{
        System.setProperty("global.config.path", "/data/lyf_dev_env");
        SystemContext.setCompanyId(30l);
        RecommendService recommendService= RecommendClient.getRecommendService("test");
       // RecommendService recommendService=HessionServiceFactory.getService
            //    ("http://192.168.8.17:8080/search/soa/SOARecommendService",RecommendService.class);
        RecommendRequest recommendRequest=new RecommendRequest(RecommendScene.HOME_BOTTOM);
        RecommendResponse recommendResponse=recommendService.recommend(recommendRequest);
        recommendRequest.setUserId(3333l);
        System.out.println(recommendResponse);

    }
}
