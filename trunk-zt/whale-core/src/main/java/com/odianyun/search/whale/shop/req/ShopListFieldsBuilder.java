package com.odianyun.search.whale.shop.req;

import com.odianyun.search.whale.api.model.ManagementType;
import com.odianyun.search.whale.api.model.Merchant;
import com.odianyun.search.whale.api.model.req.ShopListSearchRequest;
import com.odianyun.search.whale.api.model.resp.ShopSearchResponse;
import com.odianyun.search.whale.api.model.resp.ShopSearchResult;
import com.odianyun.search.whale.common.RequestFieldsBuilder;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.geo.req.GeoFieldsBuilder;
import com.odianyun.search.whale.index.api.common.IndexFieldConstants;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fishcus on 16/11/23.
 */
public class ShopListFieldsBuilder implements ShopRequestBuilder{

    @Autowired
    GeoFieldsBuilder geoFieldsBuilder;

    @Override
    public void build(ESSearchRequest esSearchRequest, ShopListSearchRequest shopRequest, ShopSearchResponse shopResponse) throws Exception{

        geoFieldsBuilder.build(esSearchRequest,shopRequest);
        /*RequestFieldsBuilder.build(esSearchRequest);
        esSearchRequest.setStart(0);
        if(shopRequest.isAdditionalHotProduct()){
            esSearchRequest.setCount(Integer.MAX_VALUE);
        }else{
            esSearchRequest.setCount(shopRequest.getNum());
        }*/

    }




}
