package com.odianyun.search.whale.shop.req;

import com.odianyun.search.whale.api.model.req.ShopListSearchRequest;
import com.odianyun.search.whale.api.model.resp.ShopSearchResponse;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.geo.req.GeoFieldsBuilder;
import com.odianyun.search.whale.geo.req.GeoSortBuilder;
import com.odianyun.search.whale.index.api.common.MerchantAreaIndexContants;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fishcus on 16/11/23.
 */
public class ShopListSortBuilder implements ShopRequestBuilder{

    public static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    GeoSortBuilder geoSortBuilder;

    @Override
    public void build(ESSearchRequest esSearchRequest, ShopListSearchRequest shopRequest, ShopSearchResponse shopResponse) throws Exception{

        geoSortBuilder.build(esSearchRequest,shopRequest);
        List<SortBuilder> sortBuilderList = new ArrayList<SortBuilder>();

        String now = sdf.format(new Date());
        FilterBuilder start = FilterBuilders.rangeFilter(MerchantAreaIndexContants.BUSINESS_TIME_START).lte(now);
        FilterBuilder end = FilterBuilders.rangeFilter(MerchantAreaIndexContants.BUSINESS_TIME_END).gte(now);
        FilterBuilder filter = FilterBuilders.boolFilter().must(start).must(end);

        sortBuilderList.add(SortBuilders.fieldSort(MerchantAreaIndexContants.BUSINESS_STATE).order(SortOrder.DESC).missing(0));
        sortBuilderList.add(SortBuilders.fieldSort(MerchantAreaIndexContants.BUSINESS_TIME_STATE).setNestedFilter(filter).missing(0).order(SortOrder.DESC).sortMode("max"));

        sortBuilderList.addAll(esSearchRequest.getSortBuilderList());

        esSearchRequest.setSortBuilderList(sortBuilderList);
        
    }




}
