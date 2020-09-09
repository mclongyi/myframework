package com.odianyun.search.whale.api.model.selectionproduct;

import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.soa.InputDTO;
import com.odianyun.soa.OutputDTO;

public interface SelectionProductSearchService {

    //选品中心接口 产品维度
    @Deprecated
    public SelectionProductSearchResponse selectionSearch(SelectionProductSearchRequest
                                                                  selectionProductSearchRequest) throws SearchException;

    //选品中心接口 商品维度
    @Deprecated
    public SelectionMerchantProductSearchResponse selectionSearch2(SelectionProductSearchRequest
                                                                           selectionProductSearchRequest) throws SearchException;

    //促销选品 商品维度
    @Deprecated
    public PromotionProductSearchResponse promotionSelectionSearch(PromotionProductSearchRequest
                                                                           promotionProductSearchRequest) throws SearchException;

    //促销选品 产品维度
    @Deprecated
    public PromotionProductSearchResponse promotionProductSelectionSearch(PromotionProductSearchRequest
                                                                                  promotionProductSearchRequest) throws SearchException;


    //=============================包装dto
    //选品中心接口 商品维度
//    OutputDTO<SelectionMerchantProductSearchResponse> selectionSearch2New(InputDTO<SelectionProductSearchRequest> inputDTO) throws SearchException;

    //选品中心接口 产品维度
    OutputDTO<SelectionProductSearchResponse> selectionSearchStandard(InputDTO<SelectionProductSearchRequest> inputDTO) throws SearchException;

    //选品中心接口 商品维度
    OutputDTO<SelectionMerchantProductSearchResponse> selectionSearch2Standard(InputDTO<SelectionProductSearchRequest> inputDTO) throws SearchException;

    //促销选品 商品维度
    OutputDTO<PromotionProductSearchResponse> promotionSelectionSearchStandard(InputDTO<PromotionProductSearchRequest> inputDTO) throws SearchException;

    //促销选品 产品维度
    OutputDTO<PromotionProductSearchResponse> promotionProductSelectionSearchStandard(InputDTO<PromotionProductSearchRequest> inputDTO) throws SearchException;


}
