package com.odianyun.search.whale.recommend;

import com.odianyun.search.whale.api.recommend.RecommendRequest;
import com.odianyun.search.whale.api.recommend.RecommendResponse;

/**
 * Created by zengfenghua on 16/12/12.
 */
public class RecommendContext {

    private RecommendRequest recommendRequest;

    private RecommendResponse recommendResponse=new RecommendResponse();

    public RecommendContext(RecommendRequest recommendRequest) {
        this.recommendRequest = recommendRequest;
    }

    public RecommendResponse getRecommendResponse() {
        return recommendResponse;
    }

}
