package com.odianyun.search.whale.data.service;

import com.odianyun.search.whale.api.model.req.HotWordRequest;
import com.odianyun.search.whale.api.model.req.SearchRequest;
import com.odianyun.search.whale.api.model.resp.HotWordResponse;

import java.util.List;
import java.util.Set;

/**
 * @author hs
 * @date 2018/9/14.
 */
public interface LyfHotWordService {
    /**
     * 随机获取N个搜索热词
     *
     * @return
     */
    HotWordResponse getHotWordDistinctRandom(HotWordRequest request);

    /**
     * 获取前N个热词
     *
     * @return
     */
    HotWordResponse getHotWordDistinct(HotWordRequest request);


    Set<String> calculateHotWordWithTx(Integer num);

    Set<String> reloadHotwordCache(Integer recal);

}
