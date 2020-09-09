package com.odianyun.search.whale.shop.resq;

import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.req.ShopListSearchRequest;
import com.odianyun.search.whale.api.model.resp.ShopSearchResponse;
import org.elasticsearch.action.search.SearchResponse;

/**
 * Created by fishcus on 16/11/22.
 */
public interface ShopResponseHandler {

    void handle(SearchResponse searchResponse, ShopSearchResponse shopResponse, ShopListSearchRequest shopRequest) throws Exception;

}
