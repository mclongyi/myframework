package com.odianyun.search.whale.api.model.req;

import com.odianyun.search.whale.api.model.geo.GeoPathRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author hs
 * @date 2018/8/28.
 */
@Data
@NoArgsConstructor
public class GeoPathSearchRequest {
    List<GeoPathRequest> geoPathRequestList;

    private List<Long> mpIds;
    private Long areaCode;
}
