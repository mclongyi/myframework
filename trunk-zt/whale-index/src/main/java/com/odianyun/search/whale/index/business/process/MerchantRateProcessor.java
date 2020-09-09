package com.odianyun.search.whale.index.business.process;

import com.odianyun.search.whale.data.model.BusinessProduct;
import com.odianyun.search.whale.data.model.Rate;
import com.odianyun.search.whale.data.service.RateService;
import com.odianyun.search.whale.index.business.process.base.BaseMerchantRateProcessor;
import com.odianyun.search.whale.index.common.ProcessorApplication;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by juzhongzheng on 2016-08-22.
 */
public class MerchantRateProcessor extends BaseMerchantRateProcessor {

    private RateService RateService;

    public MerchantRateProcessor() {
        RateService = (RateService) ProcessorApplication.getBean("RateService");
    }

    @Override
    public void calcMerchantRate(Map<Long, BusinessProduct> map, int companyId) throws Exception {
        //获得mpids
        List<Long> merchantProductIds = new ArrayList<Long>(map.keySet());
        //获得merchantRate map
        Map<Long,Rate> rateMap = RateService.getMerchantRateByIds(merchantProductIds,companyId);
        if(rateMap == null || rateMap.size() == 0){
            return;
        }

        //把数据添加到businessProduct里
        for(Map.Entry<Long,BusinessProduct> entry : map.entrySet()){
            Long merchantProductId = entry.getKey();
            BusinessProduct businessProduct = entry.getValue();
            Rate rate = rateMap.get(merchantProductId);
            if(null != rate){
                businessProduct.setRate(rate.getRate());
                businessProduct.setPositiveRate(rate.getPositiveRate());
                businessProduct.setRatingCount(rate.getRatingCount());
            }
        }
    }
}
