package com.odianyun.search.whale.geo.resp;

import com.odianyun.search.whale.api.model.req.ShopListSearchRequest;
import com.odianyun.search.whale.api.model.resp.ShopSearchResponse;
import com.odianyun.search.whale.es.api.ESService;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.index.api.common.MerchantAreaIndexContants;
import com.odianyun.search.whale.index.api.common.OplusOIndexConstants;
import com.odianyun.search.whale.shop.req.ShopRequestBuilder;
import com.odianyun.search.whale.shop.resq.ShopResponseHandler;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by Fu Yifan on 2017/11/17.
 */
public class GeoZeroResultHandler {
    private static Logger log = Logger.getLogger(GeoZeroResultHandler.class);
    int lessResultSize = 0; //如果查询的结果数大于此阈值 则不进行零少结果的处理

    @Autowired
    private List<ShopRequestBuilder> lessRequestBuilders;
    @Autowired
    private List<ShopResponseHandler> lessResponseHandlers;

    public void handle(SearchResponse esSearchResponse, ShopSearchResponse response, ShopListSearchRequest shopRequest) throws Exception {
        if (!shopRequest.isZeroResponseHandler()) {
            return;
        }
        if (esSearchResponse.getHits().getTotalHits() > lessResultSize) {
            return;
        }
        ShopSearchResponse lessResponse = new ShopSearchResponse();
        response.setCompanyId(shopRequest.getCompanyId());
        ESSearchRequest esSearchRequest = new ESSearchRequest(OplusOIndexConstants.index_alias,
                MerchantAreaIndexContants.index_type);

        for(ShopRequestBuilder builder : lessRequestBuilders){
            builder.build(esSearchRequest,shopRequest,lessResponse);
        }

        SearchResponse searchResponse = ESService.search(esSearchRequest);
        for(ShopResponseHandler handler : lessResponseHandlers){
            handler.handle(searchResponse,lessResponse,shopRequest);
        }

        response.setZeroRecommendShopResult(lessResponse.getShopResult());

    }

    public List<ShopRequestBuilder> getLessRequestBuilders() {
        return lessRequestBuilders;
    }

    public void setLessRequestBuilders(List<ShopRequestBuilder> lessRequestBuilders) {
        this.lessRequestBuilders = lessRequestBuilders;
    }

    public List<ShopResponseHandler> getLessResponseHandlers() {
        return lessResponseHandlers;
    }

    public void setLessResponseHandlers(List<ShopResponseHandler> lessResponseHandlers) {
        this.lessResponseHandlers = lessResponseHandlers;
    }
}
