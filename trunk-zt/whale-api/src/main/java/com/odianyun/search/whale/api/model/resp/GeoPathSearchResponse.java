package com.odianyun.search.whale.api.model.resp;

import com.odianyun.search.whale.api.model.geo.GeoPathRequest;
import com.odianyun.search.whale.api.model.geo.GeoPathResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author hs
 * @date 2018/8/28.
 */
@Data
@NoArgsConstructor
public class GeoPathSearchResponse {
    Map<GeoPathRequest,GeoPathResponse> multiGeoPathSearch;
    public Map<Long,Boolean> productSaleArea;
}
