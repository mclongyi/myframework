package com.odianyun.search.whale.api.model.req;

import com.odianyun.search.whale.api.model.geo.Point;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author hs
 * @date 2018/8/28.
 */
@NoArgsConstructor
@Data
public class ChildMerchantSearchRequest {
    List<Long> parentMerchantIds;
    Point point;
    Integer companyId;
}
