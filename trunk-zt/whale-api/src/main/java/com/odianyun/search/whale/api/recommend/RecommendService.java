package com.odianyun.search.whale.api.recommend;

/**
 * Created by zengfenghua on 16/12/11.
 */
public interface RecommendService {

    public RecommendResponse recommend(RecommendRequest recommendRequest) throws Exception;

}
