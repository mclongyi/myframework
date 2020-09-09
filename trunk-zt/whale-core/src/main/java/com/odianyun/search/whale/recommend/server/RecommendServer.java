package com.odianyun.search.whale.recommend.server;

import com.odianyun.search.whale.api.recommend.RecommendRequest;
import com.odianyun.search.whale.api.recommend.RecommendResponse;
import com.odianyun.search.whale.api.recommend.RecommendScene;
import com.odianyun.search.whale.api.recommend.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by zengfenghua on 16/12/11.
 */
public class RecommendServer implements RecommendService{

    @Autowired
    HomeGuessHandler homeGuessHandler;

    @Autowired
    ProductDetailHanlder productDetailHanlder;

    @Autowired
    CartHandler cartHandler;

    @Autowired
    OrderHandler orderHandler;

    @Autowired
    PersonCenterHandler personCenterHandler;


    @Override
    public RecommendResponse recommend(RecommendRequest recommendRequest) throws Exception {
        RecommendScene recommendScene=recommendRequest.getRecommendScene();
        if(recommendScene==null){
            throw new RuntimeException("recommend scene is null");
        }
        if(recommendScene.equals(RecommendScene.HOME_BOTTOM)){
            return homeGuessHandler.recommend(recommendRequest);
        }else if(recommendScene.equals(RecommendScene.PRODUCT_DETAIL)){
            return productDetailHanlder.recommend(recommendRequest);
        }else if(recommendScene.equals(RecommendScene.CART)){
            return cartHandler.recommend(recommendRequest);
        }else if(recommendScene.equals(RecommendScene.ORDER)){
            return orderHandler.recommend(recommendRequest);
        }else if(recommendScene.equals(RecommendScene.PERSON_CENTER)){
            return personCenterHandler.recommend(recommendRequest);
        }
        return null;
    }
}
