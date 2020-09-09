package com.odianyun.search.whale.shop.resq;

import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.ManagementType;
import com.odianyun.search.whale.api.model.Merchant;
import com.odianyun.search.whale.api.model.MerchantProduct;
import com.odianyun.search.whale.api.model.req.SearchByIdRequest;
import com.odianyun.search.whale.api.model.req.ShopListSearchRequest;
import com.odianyun.search.whale.api.model.req.SortType;
import com.odianyun.search.whale.api.model.resp.SearchByIdResponse;
import com.odianyun.search.whale.api.model.resp.ShopSearchResponse;
import com.odianyun.search.whale.api.model.resp.ShopSearchResult;
import com.odianyun.search.whale.api.service.SearchService;
import com.odianyun.search.whale.cache.SearchByIdCacheImpl;
import com.odianyun.search.whale.common.IndexNameManager;
import com.odianyun.search.whale.common.MerchantProductConvertor;
import com.odianyun.search.whale.common.QueryByIdBuilder;
import com.odianyun.search.whale.common.RequestFieldsBuilder;
import com.odianyun.search.whale.data.service.HotSaleMerchantProductService;
import com.odianyun.search.whale.es.api.ESService;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.index.api.common.IndexConstants;
import com.odianyun.search.whale.index.api.common.IndexFieldConstants;
import com.odianyun.search.whale.server.SearchByIdHandler;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by fishcus on 16/11/23.
 */
public class HotSaleMerchantProductHandler implements ShopResponseHandler{

    @Autowired
    HotSaleMerchantProductService hotSaleMerchantProductService;

    @Autowired
    MerchantProductConvertor merchantProductConvertor;

    /*@Autowired
    SearchService searchService;*/

    @Override
    public void handle(SearchResponse searchResponse, ShopSearchResponse shopResponse, ShopListSearchRequest shopRequest) throws Exception {
        if(!shopRequest.isAdditionalHotProduct()){
            return;
        }
        if(CollectionUtils.isEmpty(shopResponse.getShopResult())){
            return;
        }
        List<Long> mpIds = convert(shopRequest,shopResponse);
        if(CollectionUtils.isEmpty(mpIds)){
            return ;
        }
        SearchByIdRequest searchByIdRequest = new SearchByIdRequest(mpIds,shopRequest.getCompanyId());

        SearchByIdResponse searchByIdResponse = loadFromCache(searchByIdRequest);

        if(searchByIdResponse != null){
            handleResponse(searchByIdResponse,shopResponse);
        }else {
            doSearch(shopRequest, shopResponse, mpIds);
            putCache(searchByIdRequest,shopResponse);

        }

    }

    private void putCache(SearchByIdRequest searchByIdRequest, ShopSearchResponse shopResponse) {
        List<ShopSearchResult> shopResult = shopResponse.getShopResult();
        SearchByIdResponse searchByIdResponse = new SearchByIdResponse();
        List<MerchantProduct> merchantProductList = new ArrayList<>();
        searchByIdResponse.setMerchantProductList(merchantProductList);
        if(CollectionUtils.isNotEmpty(shopResult)){
            for(ShopSearchResult shopSearchResult : shopResult){
                List<MerchantProduct> mpList = shopSearchResult.getMerchantProductList();
                if(CollectionUtils.isNotEmpty(mpList)){
                    merchantProductList.addAll(mpList);
                }
            }
        }
        SearchByIdCacheImpl.instance.put(searchByIdRequest,searchByIdResponse);
    }

    private SearchByIdResponse loadFromCache(SearchByIdRequest request) {
        SearchByIdResponse response = SearchByIdCacheImpl.instance.get(request);
        return response;
    }

    private void doSearch(ShopListSearchRequest shopRequest, ShopSearchResponse shopResponse, List<Long> mpIds) throws Exception {
        ESSearchRequest esSearchRequest = new ESSearchRequest(IndexNameManager.getIndexName(),
                IndexConstants.index_type);

        buildQuery(esSearchRequest,shopResponse,shopRequest);

        SearchResponse mpResponse = ESService.search(esSearchRequest);

        handleResponse(mpResponse,shopResponse);
    }

    private List<Long> convert(ShopListSearchRequest shopRequest, ShopSearchResponse shopResponse) throws Exception {
        List<ShopSearchResult> list = shopResponse.getShopResult();

        List<Long> mpIdList = new ArrayList<>();
        for(ShopSearchResult result : list){
            Merchant merchant = result.getMerchant();
            List<Long> mpIds = getHotSaleMerchantProducts(merchant.getId(),shopRequest.getCompanyId(),shopRequest.getNum());
            if(CollectionUtils.isNotEmpty(mpIds)){
                mpIdList.addAll(mpIds);
            }
        }
        Collections.sort(mpIdList);
        return mpIdList;
    }
    /*

    private void handleResponse(Map<Long, MerchantProduct> merchantProductMap, ShopSearchResponse shopResponse) throws Exception {
        Map<Long, List<MerchantProduct>> ret = new HashMap<Long, List<MerchantProduct>>();
        for (MerchantProduct mp : merchantProductMap.values()) {
            List<MerchantProduct> merchantProductList = ret.get(mp.getMerchantId());
            if(CollectionUtils.isEmpty(merchantProductList)){
                merchantProductList = new ArrayList<>();
                ret.put(mp.getMerchantId(), merchantProductList);
            }
            merchantProductList.add(mp);
        }

        List<ShopSearchResult> shopResultList = shopResponse.getShopResult();
        for(ShopSearchResult result : shopResultList){
            Long merchantId = result.getMerchant().getId();
            List<MerchantProduct> merchantProductList = ret.get(merchantId);
            if(CollectionUtils.isNotEmpty(merchantProductList)){
                result.setMerchantProductList(merchantProductList);
            }
        }

    }*/

    private void handleResponse(SearchByIdResponse searchByIdResponse, ShopSearchResponse shopResponse) throws Exception {
        List<MerchantProduct> merchantProductList = searchByIdResponse.getMerchantProductList();
        Map<Long, List<MerchantProduct>> ret = new HashMap<Long, List<MerchantProduct>>();

        for(MerchantProduct mp : merchantProductList){
            List<MerchantProduct> list = ret.get(mp.getMerchantId());
            if(CollectionUtils.isEmpty(list)){
                list = new ArrayList<>();
                ret.put(mp.getMerchantId(), list);
            }
            merchantProductList.add(mp);
        }

        List<ShopSearchResult> shopResultList = shopResponse.getShopResult();
        for(ShopSearchResult result : shopResultList){
            Long merchantId = result.getMerchant().getId();
            List<MerchantProduct> list = ret.get(merchantId);
            if(CollectionUtils.isNotEmpty(merchantProductList)){
                result.setMerchantProductList(merchantProductList);
            }
        }
    }


    private void handleResponse(SearchResponse mpResponse, ShopSearchResponse shopResponse) throws Exception {
        SearchHits searchHits = mpResponse.getHits();
        SearchHit[] searchHitArray = searchHits.getHits();
        Map<Long, List<MerchantProduct>> ret = new HashMap<Long, List<MerchantProduct>>();

        for (SearchHit hit : searchHitArray) {
            Map<String, SearchHitField> data = hit.fields();
            MerchantProduct merchantProduct = merchantProductConvertor.convertFromSearchHitField(data);
            List<MerchantProduct> merchantProductList = ret.get(merchantProduct.getMerchantId());
            if(CollectionUtils.isEmpty(merchantProductList)){
                merchantProductList = new ArrayList<>();
                ret.put(merchantProduct.getMerchantId(), merchantProductList);
            }
            merchantProductList.add(merchantProduct);
        }

        List<ShopSearchResult> shopResultList = shopResponse.getShopResult();
        for(ShopSearchResult result : shopResultList){
            Long merchantId = result.getMerchant().getId();
            List<MerchantProduct> merchantProductList = ret.get(merchantId);
            if(CollectionUtils.isNotEmpty(merchantProductList)){
                result.setMerchantProductList(merchantProductList);
            }
        }

    }

    private void buildQuery(ESSearchRequest esSearchRequest, ShopSearchResponse shopResponse,ShopListSearchRequest shopRequest) throws Exception {
        List<ShopSearchResult> list = shopResponse.getShopResult();

        List<Long> mpIdList = new ArrayList<>();
        for(ShopSearchResult result : list){
            Merchant merchant = result.getMerchant();
            List<Long> mpIds = getHotSaleMerchantProducts(merchant.getId(),shopRequest.getCompanyId(),shopRequest.getNum());
            if(CollectionUtils.isNotEmpty(mpIds)){
                mpIdList.addAll(mpIds);
            }
        }

        if(CollectionUtils.isNotEmpty(mpIdList)){

            QueryByIdBuilder.build(esSearchRequest,mpIdList,shopRequest.getCompanyId(),0,Integer.MAX_VALUE);
            List<SortBuilder> sortBuilderList = new ArrayList<>();
            sortBuilderList.add(new FieldSortBuilder(IndexFieldConstants.VOLUME4SALE).order(SortOrder.DESC));
            esSearchRequest.setSortBuilderList(sortBuilderList);
        }
    }

    private List<Long> getHotSaleMerchantProducts(Long merchantId, Integer companyId,Integer num) throws Exception {
        List<Long> merchantIdList = new ArrayList<Long>();
        merchantIdList.add(merchantId);
        return hotSaleMerchantProductService.getHotSaleMerchantProducts(merchantIdList,companyId,0,num);
    }
}
