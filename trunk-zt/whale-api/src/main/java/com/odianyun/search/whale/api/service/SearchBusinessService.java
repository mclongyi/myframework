package com.odianyun.search.whale.api.service;

import com.odianyun.search.whale.api.model.BrandResult;
import com.odianyun.search.whale.api.model.geo.GeoPathRequest;
import com.odianyun.search.whale.api.model.geo.GeoPathResponse;
import com.odianyun.search.whale.api.model.req.BrandSearchRequest;
import com.odianyun.search.whale.api.model.req.GeoPathSearchRequest;
import com.odianyun.search.whale.api.model.req.SuggestRequest;
import com.odianyun.search.whale.api.model.resp.AreaSuggestResponse;
import com.odianyun.search.whale.api.model.resp.GeoPathSearchResponse;
import com.odianyun.soa.InputDTO;
import com.odianyun.soa.OutputDTO;

import java.util.List;
import java.util.Map;

/**
 * Created by zengfenghua on 16/12/6.
 */
public interface SearchBusinessService {

    /**
     * 根据brandName 获取brand信息
     * @param brandName
     * @return
     */
    @Deprecated
    BrandResult getBrand(String brandName);

    /**
     * 根据输入的省市区address 获取对应的areaCode(全部三级区域)
     * @param suggestRequest
     * @return
     */
    @Deprecated
    AreaSuggestResponse areaSuggest(SuggestRequest suggestRequest);

    /**
     * 根据门店id 和 配送地址 计算两者之间的步行距离
     * @param geoPathRequest
     * @return
     */
    @Deprecated
    GeoPathResponse geoPathSearch(GeoPathRequest geoPathRequest);

    /**
     * 批量geoPathSearch 接口
     * @param geoPathRequestList
     * @return
     */
    @Deprecated
    Map<GeoPathRequest,GeoPathResponse> multiGeoPathSearch(List<GeoPathRequest> geoPathRequestList);

    //判断商品是否在指定的销售区域内
    @Deprecated
    public Map<Long,Boolean> checkMerchantProductSaleArea(List<Long> mpIds,Long areaCode) throws Exception;

   /* AreaSuggestResponse merchantServiceArea (Long areaCode, Integer areaLevel) throws Exception;*/


//------------------------------SOA 标准化--------------------------------------------
    /**
     * 根据brandName 获取brand信息
     * @return
     */
    OutputDTO<BrandResult> getBrandStandard(InputDTO<BrandSearchRequest> inputDTO);

    /**
     * 根据输入的省市区address 获取对应的areaCode(全部三级区域)
     * @return
     */
    OutputDTO<AreaSuggestResponse> areaSuggestStandard(InputDTO<SuggestRequest> inputDTO);

    /**
     * 根据门店id 和 配送地址 计算两者之间的步行距离
     * @return
     */
    OutputDTO<GeoPathResponse> geoPathSearchStandard(InputDTO<GeoPathRequest> inputDTO);

    /**
     * 批量geoPathSearch 接口
     * @return
     */
    OutputDTO<GeoPathSearchResponse> multiGeoPathSearchStandard(InputDTO<GeoPathSearchRequest> inputDTO);

    //判断商品是否在指定的销售区域内
    OutputDTO<GeoPathSearchResponse> checkMerchantProductSaleAreaStandard(InputDTO<GeoPathSearchRequest> inputDTO) throws Exception;


}
