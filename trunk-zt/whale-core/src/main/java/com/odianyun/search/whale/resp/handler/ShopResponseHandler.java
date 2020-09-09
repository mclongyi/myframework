package com.odianyun.search.whale.resp.handler;

import com.odianyun.frontier.global.utils.BeanMapper;
import com.odianyun.search.whale.api.model.ShopResult;
import com.odianyun.search.whale.api.model.req.BaseSearchRequest;
import com.odianyun.search.whale.data.model.Shop;
import com.odianyun.search.whale.data.service.MerchantService;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 造搜索结果中返回关键词匹配到的店铺信息
 * Created by Fu Yifan on 2017/8/19.
 */
public class ShopResponseHandler implements ResponseHandler<BaseSearchRequest> {
    @Autowired
    MerchantService merchantService;

    @Override
    public void handle(SearchResponse esSearchResponse, com.odianyun.search.whale.api.model.SearchResponse searchResponse, ESSearchRequest esSearchRequest, BaseSearchRequest searchRequest) throws Exception {
        int companyId = searchRequest.getCompanyId();
        if (searchRequest.getKeyword() == null || searchRequest.getKeyword().trim().equals("")) {
            return;
        }
        Shop shop = merchantService.getShopByShopName(searchRequest.getKeyword(), companyId);
        if (shop != null) {
            ShopResult shopResult = BeanMapper.map(shop, ShopResult.class);
            searchResponse.setShopResult(shopResult);
        }
    }
}
