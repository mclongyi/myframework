package com.odianyun.search.whale.api.recommend;

/**
 * Created by zengfenghua on 16/12/12.
 */
public class RecommendCacheService implements RecommendService{

    private RecommendService recommendService;

    private String clientName;

    public RecommendCacheService(RecommendService recommendService,String clientName){
        this.recommendService=recommendService;
        this.clientName=clientName;
    }
    @Override
    public RecommendResponse recommend(RecommendRequest recommendRequest) throws Exception {
        if(recommendRequest.getRecommendScene()==null){
            throw  new RuntimeException("recommendRequest recommend scene is null");
        }
        return recommendService.recommend(recommendRequest);
    }
}
