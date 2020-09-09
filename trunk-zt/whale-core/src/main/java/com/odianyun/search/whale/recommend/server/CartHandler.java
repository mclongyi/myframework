package com.odianyun.search.whale.recommend.server;

import com.odianyun.architecture.caddy.SystemContext;
import com.odianyun.search.whale.api.model.MerchantProduct;
import com.odianyun.search.whale.api.model.SearchResponse;
import com.odianyun.search.whale.api.model.geo.Point;
import com.odianyun.search.whale.api.model.req.*;
import com.odianyun.search.whale.api.model.resp.HotSearchResponse;
import com.odianyun.search.whale.api.model.resp.ShopSearchResponse;
import com.odianyun.search.whale.api.model.resp.ShopSearchResult;
import com.odianyun.search.whale.api.recommend.RecommendRequest;
import com.odianyun.search.whale.api.recommend.RecommendResponse;
import com.odianyun.search.whale.api.recommend.RecommendService;
import com.odianyun.search.whale.server.HotSearchHandler;
import com.odianyun.search.whale.server.SearchByIdHandler;
import com.odianyun.search.whale.server.SearchHandler;
import com.odianyun.search.whale.server.WhaleServer;
import com.odianyun.search.whale.shop.ShopServer;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zengfenghua on 16/12/12.
 */
public class CartHandler implements RecommendService{

    @Autowired
    SearchByIdHandler searchByIdHandler;

    @Autowired
    SearchHandler searchHandler;

    @Autowired
    HotSearchHandler hotSearchHandler;

    @Autowired
    WhaleServer whaleServer;

    @Autowired
    ShopServer shopServer;

    @Override
    public RecommendResponse recommend(RecommendRequest recommendRequest) throws Exception {
        int companyId= SystemContext.getCompanyId().intValue();
        List<Long> currentVisitMpIds=recommendRequest.getCurrentVisitMpIds();
        RecommendResponse recommendResponse=new RecommendResponse();
        Point point=recommendRequest.getPoint();
        List<Long> merchantIds=new ArrayList<Long>();
        if(point!=null){
            ShopListSearchRequest shopListSearchRequest=new ShopListSearchRequest(point,companyId);
            ShopSearchResponse shopSearchResponse=shopServer.search(shopListSearchRequest);
            if(shopSearchResponse!=null){
                List<ShopSearchResult> shopSearchResults=shopSearchResponse.getShopResult();
                if(shopSearchResults!=null){
                    for(ShopSearchResult shopSearchResult:shopSearchResults){
                        merchantIds.add(shopSearchResult.getMerchant().getId());
                    }
                }
            }
        }

        if(CollectionUtils.isEmpty(merchantIds) && CollectionUtils.isNotEmpty(recommendRequest.getMerchantIds())){
            merchantIds=recommendRequest.getMerchantIds();
        }
        BaseSearchRequest searchRequest=new SearchRequest(companyId);
        searchRequest.setMerchantIdList(merchantIds);
        if(CollectionUtils.isNotEmpty(currentVisitMpIds)){
            Map<Long,MerchantProduct> merchantProductMap=
                    searchByIdHandler.searchById(recommendRequest.getCurrentVisitMpIds(),companyId);
            MerchantProduct merchantProduct=merchantProductMap.get(currentVisitMpIds.get(0));
            if(merchantProduct!=null){
                Long brandId=merchantProduct.getBrandId();
                Long categoryId=merchantProduct.getCategoryId();
                searchRequest.setStart(recommendRequest.getStart());
                searchRequest.setCount(recommendRequest.getCount());
                List<Long> brandIds=new ArrayList<>();
                brandIds.add(brandId);
                searchRequest.setBrandIds(brandIds);
                List<Long> categoryIds=new ArrayList<Long>();
                categoryIds.add(categoryId);
                searchRequest.setCategoryIds(categoryIds);
                searchRequest.setExcludeMpIds(currentVisitMpIds);
                searchRequest.setSaleAreaCode(recommendRequest.getSaleAreaCodes());
                SearchResponse searchResponse=whaleServer.search((SearchRequest)searchRequest);
                recommendResponse.setMerchantProducts(searchResponse.getMerchantProductResult());
            }
        }else{
            searchRequest.setStart(recommendRequest.getStart());
            searchRequest.setCount(recommendRequest.getCount());
            searchRequest.setKeyword("*****");
            searchRequest.setSaleAreaCode(recommendRequest.getSaleAreaCodes());
            List<SortType> sortTypeList = new ArrayList<SortType>();
            sortTypeList.add(SortType.volume4sale_desc);
            searchRequest.setSortTypeList(sortTypeList);
            SearchResponse searchResponse=whaleServer.search((SearchRequest)searchRequest);
            recommendResponse.setMerchantProducts(searchResponse.getMerchantProductResult());
        }
        return recommendResponse;
    }
}
