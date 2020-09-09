package com.odianyun.search.whale.shop;

import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.Merchant;
import com.odianyun.search.whale.api.model.geo.GeoSearchRequest;
import com.odianyun.search.whale.api.model.geo.GeoSearchResponse;
import com.odianyun.search.whale.api.model.geo.Point;
import com.odianyun.search.whale.es.api.ESService;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.geo.req.GeoQueryBuilder;
import com.odianyun.search.whale.geo.req.GeoSortBuilder;
import com.odianyun.search.whale.geo.resp.GeoFieldsResponseHandler;
import com.odianyun.search.whale.index.api.common.MerchantAreaIndexContants;
import com.odianyun.search.whale.index.api.common.MerchantAreaIndexFieldContants;
import com.odianyun.search.whale.index.api.common.OplusOIndexConstants;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zengfenghua on 17/3/8.
 */
public class GetChildMerchantByPointHandler {


    @Autowired
    GeoQueryBuilder geoQueryBuilder;

    @Autowired
    GeoSortBuilder geoSortBuilder;

    @Autowired
    GeoFieldsResponseHandler geoFieldsResponseHandler;


    public Map<Long, List<Merchant>> getChildMerchantByPoint(List<Long> parentMerchantIds, Point point, Integer companyId) throws Exception {
        List<Merchant> merchants = getChildMerchantSortedByDistance(parentMerchantIds,point,companyId);
        Map<Long, List<Merchant>> retMap=new HashMap<Long, List<Merchant>>();
        if(CollectionUtils.isEmpty(merchants)){
            return retMap;
        }
        for(Merchant merchant:merchants){
            List<Merchant> merchantList=retMap.get(merchant.getParentId());
            if(merchantList==null){
                merchantList=new ArrayList<Merchant>();
                retMap.put(merchant.getParentId(),merchantList);
            }
            merchantList.add(merchant);
        }
        return retMap;
    }


    public List<Merchant> getChildMerchantSortedByDistance(List<Long> parentMerchantIds, Point point, Integer companyId) throws Exception {
        ESSearchRequest esSearchRequest = new ESSearchRequest(OplusOIndexConstants.index_alias,
                MerchantAreaIndexContants.index_type);
        GeoSearchRequest geoSearchRequest=new GeoSearchRequest(point,companyId);
        geoQueryBuilder.build(esSearchRequest,geoSearchRequest);
        geoSortBuilder.build(esSearchRequest,geoSearchRequest);
        BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();
        BoolQueryBuilder parentMerchantIdBoolQueryBuilder=new BoolQueryBuilder();
        for(Long parentMerchantId:parentMerchantIds){
            parentMerchantIdBoolQueryBuilder.should(new TermQueryBuilder(MerchantAreaIndexFieldContants.PARENT_MERCHANT_ID,parentMerchantId));
        }
        boolQueryBuilder.must(parentMerchantIdBoolQueryBuilder);
        boolQueryBuilder.must(esSearchRequest.getQueryBuilder());
        esSearchRequest.setQueryBuilder(boolQueryBuilder);
        esSearchRequest.setCount(1000);
        SearchResponse searchResponse = ESService.search(esSearchRequest);
        GeoSearchResponse geoSearchResponse = new GeoSearchResponse();
        geoSearchResponse.setCompanyId(companyId);
        geoFieldsResponseHandler.handle(searchResponse,geoSearchResponse);
        List<Merchant> merchants = geoSearchResponse.getMerchants();
        return merchants;
    }
}
