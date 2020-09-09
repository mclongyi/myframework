package com.odianyun.search.whale.shop.resq;

import com.odianyun.search.whale.api.model.ManagementType;
import com.odianyun.search.whale.api.model.Merchant;
import com.odianyun.search.whale.api.model.MerchantProduct;
import com.odianyun.search.whale.api.model.geo.GeoSearchResponse;
import com.odianyun.search.whale.api.model.req.ShopListSearchRequest;
import com.odianyun.search.whale.api.model.resp.ShopSearchResponse;
import com.odianyun.search.whale.api.model.resp.ShopSearchResult;
import com.odianyun.search.whale.common.MerchantProductConvertor;
import com.odianyun.search.whale.data.service.HotSaleMerchantProductService;
import com.odianyun.search.whale.es.api.ESService;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.geo.resp.GeoFieldsResponseHandler;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.index.api.common.IndexFieldConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fishcus on 16/11/23.
 */
public class ShopListFieldsHandler implements ShopResponseHandler{

    @Autowired
    GeoFieldsResponseHandler geoFieldsResponseHandler;

    @Autowired
    MerchantProductConvertor merchantProductConvertor;

    @Override
    public void handle(SearchResponse searchResponse, ShopSearchResponse shopResponse, ShopListSearchRequest shopRequest) throws Exception {

        GeoSearchResponse geoSearchResponse = new GeoSearchResponse();
        geoSearchResponse.setCompanyId(shopRequest.getCompanyId());
        geoFieldsResponseHandler.handle(searchResponse,geoSearchResponse);
        shopResponse.setTotalHit(searchResponse.getHits().getTotalHits());

        List<Merchant> merchants = geoSearchResponse.getMerchants();

        if(CollectionUtils.isEmpty(merchants)){
            return ;
        }

        SearchHit[] searchHitArray = searchResponse.getHits().getHits();
        Map<Long, List<MerchantProduct>> ret = new HashMap<Long, List<MerchantProduct>>();

            for (SearchHit hit : searchHitArray) {
                Map<String, SearchHits> innerHitsMap = hit.getInnerHits();
                if(innerHitsMap == null || innerHitsMap.size() == 0){
                    continue;
                }
                SearchHits innerHits = innerHitsMap.get(IndexConstants.index_type);
                SearchHit[] innerArray = innerHits.getHits();
                for(SearchHit innerHit : innerArray){
                    Map<String, SearchHitField> data = innerHit.fields();
//                    Map<String, SearchHitField> data =innerHit.getSource();
                    MerchantProduct merchantProduct = merchantProductConvertor.convertFromSearchHitField(data);
                    SearchHitField field = data.get(IndexFieldConstants.MERCHANT_CATEGORYID);
                    if( field != null && field.getValues() != null && field.getValues().size() > 0){
                        String merchantCategoryId = (String) field.getValues().get(0);
                        if(StringUtils.isNotBlank(merchantCategoryId)){
                            merchantProduct.setMerchantCategoryId(Long.valueOf(merchantCategoryId));
                        }
                    }
                    List<MerchantProduct> merchantProductList = ret.get(merchantProduct.getMerchantId());
                    if(CollectionUtils.isEmpty(merchantProductList)){
                        merchantProductList = new ArrayList<>();
                        ret.put(merchantProduct.getMerchantId(), merchantProductList);
                    }
                    merchantProductList.add(merchantProduct);
                }
            }

        List<ShopSearchResult> shopResultList = new ArrayList<>();
        for(Merchant merchant : merchants){
            ShopSearchResult shopResult = new ShopSearchResult();
            shopResult.setMerchant(merchant);
            shopResult.setMerchantProductList(ret.get(merchant.getId()));
            shopResultList.add(shopResult);
        }
        shopResponse.setShopResult(shopResultList);

    }

}
