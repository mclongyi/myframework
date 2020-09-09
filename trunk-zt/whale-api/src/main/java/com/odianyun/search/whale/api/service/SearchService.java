package com.odianyun.search.whale.api.service;

import java.util.List;
import java.util.Map;

import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.MerchantProduct;
import com.odianyun.search.whale.api.model.SearchResponse;
import com.odianyun.search.whale.api.model.req.*;
import com.odianyun.search.whale.api.model.resp.*;
import com.odianyun.search.whale.api.model.series.SeriesRequest;
import com.odianyun.search.whale.api.model.series.SeriesResponse;
import com.odianyun.soa.InputDTO;
import com.odianyun.soa.OutputDTO;

public interface SearchService {

    //商品搜索
    @Deprecated
    SearchResponse search(SearchRequest searchRequest) throws SearchException;

    // 附近热门商品搜索
    @Deprecated
    HotSearchResponse hotSearch(HotSearchRequest searchRequest) throws SearchException;

    //商品搜索(不包含聚类请求,零少结果处理,热词推荐)
    @Deprecated
    SearchResponse searchProducts(SearchRequest searchRequest) throws SearchException;

    //店铺内搜索商品
    @Deprecated
    SearchResponse shopSearch(ShopSearchRequest shopRequest) throws SearchException;

    //根据id返回对应MerchantProduct
    Map<Long, MerchantProduct> searchById(List<Long> mpIds, Integer companyId) throws SearchException;

    //根据id返回对应MerchantProduct,可以设置缓存
    @Deprecated
    Map<Long, MerchantProduct> searchByIdNew(SearchByIdRequest request) throws SearchException;

    //根据mpCode返回对应MerchantProduct
    Map<String, MerchantProduct> searchByMpCodes(List<String> mpCodes, Integer companyId);

    //根据seriesRequest返回对应的SeriesResponse
    @Deprecated
    SeriesResponse searchBySeriesRequest(SeriesRequest seriesRequest) throws SearchException;

    // 促销搜索
    @Deprecated
    PromotionSearchResponse promotionSearch(PromotionSearchRequest searchRequest) throws SearchException;

    // 促销类型搜索
    @Deprecated
    PromotionTypeSearchResponse promotionTypeSearch(PromotionTypeSearchRequest searchRequest) throws SearchException;

    // 积分商城搜索
    @Deprecated
    SearchResponse pointMpSearch(PointSearchRequest searchRequest) throws SearchException;


    //====================================包装成标准soa模式===================================================

    //根据id返回对应MerchantProduct
//    OutputDTO<SearchByIdResponse> searchByIdNew2(InputDTO<SearchByIdRequest> inputDTO) throws SearchException;
//
//    //    商品搜索
//    OutputDTO<SearchResponse> searchNew(InputDTO<SearchRequest> inputDTO) throws SearchException;
//
//    //    根据mpCode返回对应MerchantProduct
//    OutputDTO<SearchByCodeResponse> searchByMpCodesNew(InputDTO<SearchByCodeRequest> inputDTO);
//
//    //     积分商城搜索
//    OutputDTO<SearchResponse> pointMpSearchNew(InputDTO<PointSearchRequest> inputDTO) throws SearchException;

    //商品搜索
    OutputDTO<SearchResponse> searchStandard(InputDTO<SearchRequest> inputDTO) throws SearchException;

    // 附近热门商品搜索
    OutputDTO<HotSearchResponse> hotSearchStandard(InputDTO<HotSearchRequest> inputDTO) throws SearchException;

    //商品搜索(不包含聚类请求,零少结果处理,热词推荐)
    OutputDTO<SearchResponse> searchProductsStandard(InputDTO<SearchRequest> inputDTO) throws SearchException;

    //店铺内搜索商品
    OutputDTO<SearchResponse> shopSearchStandard(InputDTO<ShopSearchRequest> inputDTO) throws SearchException;

    //根据id返回对应MerchantProduct
    OutputDTO<SearchByIdResponse> searchByIdStandard(InputDTO<SearchByIdRequest> inputDTO) throws SearchException;

    //根据id返回对应MerchantProduct,可以设置缓存
    OutputDTO<SearchByIdResponse> searchByIdNewStandard(InputDTO<SearchByIdRequest> inputDTO) throws SearchException;

    //根据mpCode返回对应MerchantProduct
    OutputDTO<SearchByCodeResponse> searchByMpCodesStandard(InputDTO<SearchByCodeRequest> inputDTO);

    //根据seriesRequest返回对应的SeriesResponse
    OutputDTO<SeriesResponse> searchBySeriesRequestStandard(InputDTO<SeriesRequest> inputDTO) throws SearchException;

    // 促销搜索
    OutputDTO<PromotionSearchResponse> promotionSearchStandard(InputDTO<PromotionSearchRequest> inputDTO) throws SearchException;

    // 促销类型搜索
    OutputDTO<PromotionTypeSearchResponse> promotionTypeSearchStandard(InputDTO<PromotionTypeSearchRequest> inputDTO) throws SearchException;

    // 积分商城搜索
    OutputDTO<SearchResponse> pointMpSearchStandard(InputDTO<PointSearchRequest> inputDTO) throws SearchException;

    //====================================包装成标准soa模式===================================================
}
