package com.odianyun.search.whale.shop.req;

import com.odianyun.search.whale.api.model.geo.Point;
import com.odianyun.search.whale.api.model.req.ShopListSearchRequest;
import com.odianyun.search.whale.api.model.resp.ShopSearchResponse;
import com.odianyun.search.whale.data.service.ConfigService;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.index.api.common.IndexFieldConstants;
import com.odianyun.search.whale.index.api.common.MerchantAreaIndexContants;
import com.odianyun.search.whale.index.api.common.MerchantAreaIndexFieldContants;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FilteredQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceRangeFilterBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 当当前位置没有在服务范围内时，在距离范围内推荐门店
 * Created by Fu Yifan on 2017/11/20.
 */
public class ZeroShopListFieldsBuilder implements ShopRequestBuilder {
    static final int DEFAULT_MERCHANT_FLAG = 1;
    static final int MAX_RECOMMEND_DISTANCE = 30;
    @Autowired
    ConfigService configService;

    @Override
    public void build(ESSearchRequest esSearchRequest, ShopListSearchRequest searchRequest, ShopSearchResponse shopResponse) throws Exception {
        Integer companyId = searchRequest.getCompanyId();
        if (companyId == null) {
            return;
        }
        Point point = searchRequest.getPoint();
        Double longitude=null;
        Double latitude=null;
        if(point!=null){
            longitude=point.getLongitude();
            latitude=point.getLatitude();
        }

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.must(new TermQueryBuilder(MerchantAreaIndexFieldContants.MERCHANT_FLAG,DEFAULT_MERCHANT_FLAG));
        TermQueryBuilder termQuery = new TermQueryBuilder(IndexFieldConstants.COMPANYID,companyId);
        boolQueryBuilder.must(termQuery);

        String from = "0km";
        Integer toInt = configService.getInt("max_recommend_distance", companyId);
        if (toInt == null || toInt == 0) {
            toInt = MAX_RECOMMEND_DISTANCE;
        }
        String to = toInt + "km";
        GeoDistanceRangeFilterBuilder geoDistanceRangeFilterBuilder=new GeoDistanceRangeFilterBuilder(MerchantAreaIndexContants.index_type
                +"."+MerchantAreaIndexFieldContants.LOCATION);
        geoDistanceRangeFilterBuilder.point(latitude,longitude).from(from).to(to);
        FilteredQueryBuilder filteredQueryBuilder=new FilteredQueryBuilder(boolQueryBuilder,geoDistanceRangeFilterBuilder);
        esSearchRequest.setQueryBuilder(filteredQueryBuilder);

    }
}
