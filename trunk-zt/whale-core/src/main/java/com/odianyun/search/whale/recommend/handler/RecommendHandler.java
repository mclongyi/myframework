package com.odianyun.search.whale.recommend.handler;

import com.odianyun.search.whale.api.recommend.RecommendRequest;
import com.odianyun.search.whale.recommend.RecommendContext;

/**
 * Created by zengfenghua on 16/12/11.
 */
public interface RecommendHandler {

    public void handle(RecommendContext recommendContext);
}
