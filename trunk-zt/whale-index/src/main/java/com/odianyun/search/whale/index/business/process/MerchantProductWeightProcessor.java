package com.odianyun.search.whale.index.business.process;

import com.odianyun.search.whale.data.model.BusinessProduct;
import com.odianyun.search.whale.data.service.ConfigService;
import com.odianyun.search.whale.data.service.SeasonCategoryWeightService;
import com.odianyun.search.whale.index.business.process.base.BaseMerchantProductWeightProcessor;
import com.odianyun.search.whale.index.common.ProcessorApplication;

import java.util.Map;

/**
 * Created by zengfenghua on 16/11/5.
 */
public class MerchantProductWeightProcessor extends BaseMerchantProductWeightProcessor{

    SeasonCategoryWeightService seasonCategoryWeightService;

    ConfigService configService;

    public MerchantProductWeightProcessor(){
        seasonCategoryWeightService = (SeasonCategoryWeightService) ProcessorApplication.getBean("seasonCategoryWeightService");
        configService = (ConfigService) ProcessorApplication.getBean("configService");
    }

    @Override
    public void calcMerchantProductWeight(Map<Long, BusinessProduct> businessProductMap, int companyId){
        boolean IS_SEASON_WEIGHT = configService.getBool("is_season_weight",false,companyId);
        if(!IS_SEASON_WEIGHT){
            return;
        }
        for(Map.Entry<Long,BusinessProduct> entry:businessProductMap.entrySet()){
            Long categoryId=entry.getValue().getCategoryId();
            if(categoryId!=null){
                int weight= seasonCategoryWeightService.getWeight(companyId,categoryId);
                entry.getValue().setSeasonWeight(weight);
            }
        }

    }
}
