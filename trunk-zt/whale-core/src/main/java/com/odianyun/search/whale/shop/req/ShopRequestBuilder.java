package com.odianyun.search.whale.shop.req;

import com.odianyun.search.whale.api.model.req.ShopListSearchRequest;
import com.odianyun.search.whale.api.model.resp.ShopSearchResponse;
import com.odianyun.search.whale.es.request.ESSearchRequest;

/**
 * Created by fishcus on 16/11/22.
 */
public interface ShopRequestBuilder {

    void build(ESSearchRequest esSearchRequest, ShopListSearchRequest shopRequest,ShopSearchResponse shopResponse) throws Exception;

}
