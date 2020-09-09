package com.odianyun.search.whale.api.model.resp;

import com.odianyun.search.whale.api.model.Merchant;
import com.odianyun.search.whale.api.model.geo.Point;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author hs
 * @date 2018/8/28.
 */
@NoArgsConstructor
@Data
public class ChildMerchantSearchResponse {
    Map<Long,List<Merchant>> childMerchantByPoint;
    List<Merchant> merchantList;
}
