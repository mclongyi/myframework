package com.odianyun.search.whale.data.service;

import com.odianyun.search.whale.data.model.Rate;

import java.util.List;
import java.util.Map;

/**
 * Created by juzhongzheng on 2016-08-22.
 */
public interface RateService {
    /*
    * 根据商品id查询评分情况
    * */
    public Map<Long ,Rate> getMerchantRateByIds(List<Long> merchantProductIds, int companyId) throws Exception;
}
