package com.odianyun.search.whale.server;

import com.odianyun.search.whale.api.common.PointConstants;
import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.Merchant;
import com.odianyun.search.whale.api.model.MerchantProduct;
import com.odianyun.search.whale.api.model.geo.GeoSearchRequest;
import com.odianyun.search.whale.api.model.geo.GeoSearchResponse;
import com.odianyun.search.whale.api.model.geo.Point;
import com.odianyun.search.whale.api.model.req.HotSearchRequest;
import com.odianyun.search.whale.api.model.req.SearchByIdRequest;
import com.odianyun.search.whale.api.model.req.SortType;
import com.odianyun.search.whale.api.model.resp.HotSearchResponse;
import com.odianyun.search.whale.api.model.resp.SearchByIdResponse;
import com.odianyun.search.whale.cache.SearchByIdCacheImpl;
import com.odianyun.search.whale.common.IndexNameManager;
import com.odianyun.search.whale.common.MerchantProductConvertor;
import com.odianyun.search.whale.common.QueryByIdBuilder;
import com.odianyun.search.whale.data.service.HotSaleMerchantProductService;
import com.odianyun.search.whale.es.api.ESService;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.geo.GeoSearchServer;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.index.api.common.IndexFieldConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.UnknownHostException;
import java.util.*;

/**
 * Created by fishcus on 16/11/22.
 */
public class HotSearchHandler {

    static final int MAX_NUM = 50;
    static Logger logger = Logger.getLogger(HotSearchHandler.class);

    static final int START = 0;
    static final int COUNT = 10;

    @Autowired
    GeoSearchServer geoSearchServer;

    @Autowired
    MerchantProductConvertor merchantProductConvertor;

    @Autowired
    HotSaleMerchantProductService hotSaleMerchantProductService;

    public HotSearchResponse handle(HotSearchRequest searchRequest) throws SearchException {
        HotSearchResponse response = new HotSearchResponse();
        response.setCompanyId(searchRequest.getCompanyId());
        if(!validateRequest(searchRequest)){
            return response;
        }
        try {
            List<Long> mpIds = convert(searchRequest);
            if(CollectionUtils.isEmpty(mpIds)){
                return response;
            }

            SearchByIdRequest searchByIdRequest = buildSearchByIdRequest(mpIds,searchRequest);

            SearchByIdResponse searchByIdResponse = loadFromCache(searchByIdRequest);

            if(searchByIdResponse != null){
                handleResponse(searchByIdResponse,response);
            }else{
                doSearch(searchRequest,mpIds,response);
                putCache(searchByIdRequest,response);
            }


        }catch(Exception e){
            logger.error(e.getMessage(), e);
        }


        return response;
    }

    private void putCache(SearchByIdRequest request, HotSearchResponse response) {
        SearchByIdResponse searchByIdResponse = new SearchByIdResponse();
        searchByIdResponse.setMerchantProductList(response.getMerchantProductResult());
        searchByIdResponse.setTotalHit(response.getTotalHit());
        SearchByIdCacheImpl.instance.put(request,searchByIdResponse);
    }

    private SearchByIdResponse loadFromCache(SearchByIdRequest request) {
        SearchByIdResponse response = SearchByIdCacheImpl.instance.get(request);
        return response;
    }

    private SearchByIdRequest buildSearchByIdRequest(List<Long> mpIds, HotSearchRequest searchRequest) {
        SearchByIdRequest request = new SearchByIdRequest(mpIds,searchRequest.getCompanyId());
        request.setStart(searchRequest.getStart());
        request.setCount(searchRequest.getCount());
        List<SortType> sortTypeList = new ArrayList<>();
        sortTypeList.add(SortType.volume4sale_desc);
        request.setSortTypeList(sortTypeList);
        return request;
    }

    private void doSearch(HotSearchRequest searchRequest, List<Long> mpIds,HotSearchResponse response) throws Exception {
        ESSearchRequest esSearchRequest = new ESSearchRequest(IndexNameManager.getIndexName(),
                IndexConstants.index_type);
        buildQuery(esSearchRequest,searchRequest,mpIds);

        SearchResponse mpResponse = ESService.search(esSearchRequest);

        handleResponse(mpResponse,response);

    }

    private void handleResponse(SearchByIdResponse searchByIdResponse, HotSearchResponse response) throws Exception {
        List<Long> merchantProductIds = new ArrayList<>();
        List<MerchantProduct> mpList = searchByIdResponse.getMerchantProductList();
        for(MerchantProduct mp : mpList){
            merchantProductIds.add(mp.getId());
        }
        response.setTotalHit(searchByIdResponse.getTotalHit());
        response.setMerchantProductResult(searchByIdResponse.getMerchantProductList());
        response.setMerchantProductIds(merchantProductIds);
    }

    private void handleResponse(SearchResponse mpResponse, HotSearchResponse response) throws Exception {
        SearchHits searchHits = mpResponse.getHits();
        SearchHit[] searchHitArray = searchHits.getHits();
        List<MerchantProduct> merchantProductList = new ArrayList<>();
        List<Long> merchantProductIds = new ArrayList<>();
        for (SearchHit hit : searchHitArray) {
            merchantProductIds.add(Long.valueOf(hit.getId()));

            Map<String, SearchHitField> data = hit.fields();
            MerchantProduct merchantProduct = merchantProductConvertor.convertFromSearchHitField(data);
            merchantProductList.add(merchantProduct);
        }
        response.setTotalHit(searchHits.totalHits());
        response.setMerchantProductResult(merchantProductList);
        response.setMerchantProductIds(merchantProductIds);
    }

    private void buildQuery(ESSearchRequest esSearchRequest,HotSearchRequest searchRequest, List<Long> mpIdList) {

        if(CollectionUtils.isNotEmpty(mpIdList)){

            QueryByIdBuilder.build(esSearchRequest,mpIdList,searchRequest.getCompanyId(),searchRequest.getStart(),searchRequest.getCount());

            List<SortBuilder> sortBuilderList = new ArrayList<>();
            sortBuilderList.add(new FieldSortBuilder(IndexFieldConstants.VOLUME4SALE).order(SortOrder.DESC));
            esSearchRequest.setSortBuilderList(sortBuilderList);
        }
    }

    private List<Long> convert(HotSearchRequest searchRequest) throws Exception {
        List<Long> merchantProductIdList = new ArrayList<Long>();

        List<Long> merchantIdList = searchRequest.getMerchantIdList();
        if(CollectionUtils.isEmpty(merchantIdList) && null != searchRequest.getPoint()){
            merchantIdList = searchMerchants(searchRequest);
        }
        int start = START;
        int count = COUNT;
        /*if(CollectionUtils.isNotEmpty(merchantIdList)){
            start = searchRequest.getStart();
            count = searchRequest.getCount();
        }*/

        merchantProductIdList.addAll(hotSaleMerchantProductService.getHotSaleMerchantProducts(merchantIdList,searchRequest.getCompanyId(),start,count));
        Collections.sort(merchantProductIdList);
        return merchantProductIdList;
    }

    private Map<Long,List<Long>> convertMap(HotSearchRequest searchRequest) throws Exception {
        Map<Long,List<Long>> merchantProductIdMap = new HashMap<>();

        List<Long> merchantIdList = searchRequest.getMerchantIdList();
        if(CollectionUtils.isEmpty(merchantIdList) && null != searchRequest.getPoint()){
            merchantIdList = searchMerchants(searchRequest);
        }
        int start = START;
        int count = COUNT;
        if(CollectionUtils.isNotEmpty(merchantIdList)){
            start = searchRequest.getStart();
            count = searchRequest.getCount();
        }

        merchantProductIdMap.putAll(hotSaleMerchantProductService.getHotSaleMerchantProductMap(merchantIdList,searchRequest.getCompanyId(),start,count));

        return merchantProductIdMap;
    }

    private List<Long> searchMerchants(HotSearchRequest searchRequest) {
        List<Long> merchantIdList = new ArrayList<>();
        GeoSearchRequest geoRequest = new GeoSearchRequest(searchRequest.getPoint(),searchRequest.getCompanyId());
        // 取附近最近10家门店的商品
        geoRequest.setStart(START);
        geoRequest.setCount(COUNT);
        GeoSearchResponse geoSearchResponse = geoSearchServer.search(geoRequest);
        List<Merchant> merchants = geoSearchResponse.getMerchants();
        if(CollectionUtils.isNotEmpty(merchants)){
            for(Merchant merchant : merchants){
                merchantIdList.add(merchant.getId());
            }
        }

        return merchantIdList;
    }


    private boolean validateRequest(HotSearchRequest searchRequest){
        if(searchRequest.getCompanyId()==null){
            return false;
        }
        List<Long> merchantIdList = searchRequest.getMerchantIdList();
        if(CollectionUtils.isNotEmpty(merchantIdList) && merchantIdList.size() > 200){
            return false;
        }
        if(null == searchRequest.getPoint()){
            return false;
        }
        Point point = searchRequest.getPoint();
        if(point != null){
            Double latitude = point.getLatitude();
            if(latitude == null || latitude < PointConstants.MIN_LATITUDE || latitude > PointConstants.MAX_LATITUDE){
                return false;
            }
            Double longitude = point.getLongitude();
            if(longitude == null || longitude < PointConstants.MIN_LONGITUDE || longitude > PointConstants.MAX_LONGITUDE){
                return false;
            }
        }
        return true;
    }
}
