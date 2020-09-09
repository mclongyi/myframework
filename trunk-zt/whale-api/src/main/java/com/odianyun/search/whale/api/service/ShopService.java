package com.odianyun.search.whale.api.service;

import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.Merchant;
import com.odianyun.search.whale.api.model.geo.Point;
import com.odianyun.search.whale.api.model.req.ChildMerchantSearchRequest;
import com.odianyun.search.whale.api.model.req.ShopListSearchRequest;
import com.odianyun.search.whale.api.model.resp.ChildMerchantSearchResponse;
import com.odianyun.search.whale.api.model.resp.ShopSearchResponse;
import com.odianyun.soa.InputDTO;
import com.odianyun.soa.OutputDTO;

import java.util.List;
import java.util.Map;

/**
 * Created by fishcus on 16/11/22.
 */
public interface ShopService {
    // 搜索店铺和店铺内商品
    @Deprecated
    ShopSearchResponse search(ShopListSearchRequest shopRequest) throws SearchException;


    /**
     * 根据父商家id和当前位置返回覆盖当前点的门店列表
     * @param parentMerchantIds
     * @param point
     * @param companyId
     * @return   parentMerchantId---->List<ChildMerchant>
     * @throws SearchException
     */
    @Deprecated
    public Map<Long,List<Merchant>> getChildMerchantByPoint(List<Long> parentMerchantIds, Point point, Integer companyId) throws Exception;


    /**
     * 根据父商家id和当前位置返回覆盖当前点的门店列表
     * @param parentMerchantIds
     * @param point
     * @param companyId
     * @return   List<ChildMerchant>
     * @throws SearchException
     */
    @Deprecated
    public List<Merchant> getChildMerchantSortedByDistance(List<Long> parentMerchantIds, Point point, Integer companyId) throws Exception;


    //----------------SOA包装----------------------
    // 搜索店铺和店铺内商品
    OutputDTO<ShopSearchResponse> searchStandard(InputDTO<ShopListSearchRequest> shopRequest) throws SearchException;


    /**
     * 根据父商家id和当前位置返回覆盖当前点的门店列表
     * @return   parentMerchantId---->List<ChildMerchant>
     * @throws SearchException
     */
     OutputDTO<ChildMerchantSearchResponse> getChildMerchantByPointStandard(InputDTO<ChildMerchantSearchRequest> inputDTO) throws Exception;


    /**
     * 根据父商家id和当前位置返回覆盖当前点的门店列表
     * @return   List<ChildMerchant>
     * @throws SearchException
     */
    OutputDTO<ChildMerchantSearchResponse> getChildMerchantSortedByDistanceStandard(InputDTO<ChildMerchantSearchRequest> inputDTO) throws Exception;


}
