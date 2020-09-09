package com.odianyun.search.whale.api.model.geo;

import java.util.List;
import java.util.Map;

import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.geo.GeoSearchRequest;
import com.odianyun.search.whale.api.model.geo.GeoSearchResponse;
import com.odianyun.search.whale.api.model.req.CheckIntersectionRequest;
import com.odianyun.search.whale.api.model.resp.CheckIntersectionResponse;
import com.odianyun.soa.InputDTO;
import com.odianyun.soa.OutputDTO;

/**
 * 地理位置搜索请求
 *
 * @author zengfenghua
 */
public interface GeoSearchService {

    /**
     * @param geoSearchRequest
     * @return
     * @throws SearchException
     */
    @Deprecated
    public GeoSearchResponse search(GeoSearchRequest geoSearchRequest) throws SearchException;

    /**
     * 检查这些坐标点是否落在指定商家的覆盖区域
     *
     * @param merchantId
     * @param points
     * @return
     * @throws SearchException
     */
    @Deprecated
    public Map<Point, Boolean> checkIntersection(Long merchantId, List<Point> points, Integer companyId) throws SearchException;


    /**
     * 检查这些商家的覆盖范围是否包含指定的点
     *
     * @param merchantIds
     * @param point
     * @return <MerchantId,覆盖范围是否包含指定的点>
     * @throws SearchException
     */
    @Deprecated
    public Map<Long, Boolean> checkIntersection2(List<Long> merchantIds, Point point, Integer companyId) throws SearchException;


//--------------------------------SOA包装-----------------------------------------

    OutputDTO<GeoSearchResponse> searchStandard(InputDTO<GeoSearchRequest> inputDTO) throws SearchException;

    /**
     * 检查这些坐标点是否落在指定商家的覆盖区域
     *
     * @return
     * @throws SearchException
     */
    OutputDTO<CheckIntersectionResponse> checkIntersectionStandard(InputDTO<CheckIntersectionRequest> inputDTO) throws SearchException;


    /**
     * 检查这些商家的覆盖范围是否包含指定的点
     *
     * @return <MerchantId,覆盖范围是否包含指定的点>
     * @throws SearchException
     */
    OutputDTO<CheckIntersectionResponse> checkIntersection2Standard(InputDTO<CheckIntersectionRequest> inputDTO) throws SearchException;


}
