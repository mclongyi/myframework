package com.odianyun.search.whale.data.service.impl;

import com.odianyun.search.whale.data.dao.RateDao;
import com.odianyun.search.whale.data.model.Rate;
import com.odianyun.search.whale.data.service.RateService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by juzhongzheng on 2016-08-22.
 */
public class RateServiceImpl implements RateService {
    @Autowired
    private RateDao rateDao;

    //将
    @Override
    public Map<Long, Rate> getMerchantRateByIds(List<Long> merchantProductIds, int companyId) throws Exception {
        List<Rate> rateList = rateDao.queryMerchantRateByIds(merchantProductIds,companyId);
        Map<Long,Rate> rateMap = new HashMap<>();
        for(Rate rate :rateList){
            rateMap.put(rate.getMpId(),rate);
        }
        return rateMap;
    }
}
