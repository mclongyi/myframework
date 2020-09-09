package com.odianyun.search.whale.index.business.process;

//import com.odianyun.agent.business.read.manage.impl.CommissionManageImpl;
//import com.odianyun.agent.model.MpCommission;
//import com.odianyun.agent.model.ValidateMpResultDTO;
import com.odianyun.architecture.caddy.SystemContext;
import com.odianyun.search.whale.common.remote.AgentRemoteService;
import com.odianyun.search.whale.common.remote.dto.MpCommission;
import com.odianyun.search.whale.common.remote.dto.ValidateMpResultDTO;
import com.odianyun.search.whale.data.model.BusinessProduct;
import com.odianyun.search.whale.data.service.ConfigService;
import com.odianyun.search.whale.index.business.process.base.BaseDistributionProcessor;
import com.odianyun.search.whale.index.common.ProcessorApplication;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zengfenghua on 17/5/16.
 */

/**
 * 分销相关的计算
 */
public class DistributionProcessor extends BaseDistributionProcessor {

//    CommissionManageImpl commissionManageImpl;

    ConfigService configService;

    private AgentRemoteService agentRemoteService;

    public DistributionProcessor(){
//        commissionManageImpl= (CommissionManageImpl) ProcessorApplication.getBean("commissionManageImpl");
        agentRemoteService= (AgentRemoteService) ProcessorApplication.getBean("agentRemoteService");
        configService = (ConfigService) ProcessorApplication.getBean("configService");
    }

    @Override
    public void calcDistribution(Map<Long, BusinessProduct> map, int companyId) throws Exception {
        Boolean is_calc_distribution= configService.getBool("is_calc_distribution",true,companyId);
        if(!is_calc_distribution){
            return;
        }
        SystemContext.setCompanyId(new Long(companyId));
        List<Long> mpIds=new ArrayList(map.keySet());
//        List<ValidateMpResultDTO> validateMpResultDTOs=commissionManageImpl.validateMpInCommission(mpIds);
        List<ValidateMpResultDTO> validateMpResultDTOs=agentRemoteService.validateMpInCommission(mpIds);

        if(CollectionUtils.isNotEmpty(validateMpResultDTOs)){
            for(ValidateMpResultDTO validateMpResultDTO:validateMpResultDTOs){
                long mpId=validateMpResultDTO.getMpId();
                BusinessProduct businessProduct=map.get(mpId);
                if(businessProduct!=null){
                    businessProduct.setIsDistributionMp(validateMpResultDTO.getCode());
                }
            }
        }
//        List<MpCommission> mpCommissions=commissionManageImpl.queryOrderMpTotalMoney(mpIds);
        List<MpCommission> mpCommissions=agentRemoteService.queryOrderMpTotalMoney(mpIds);
        if(CollectionUtils.isNotEmpty(mpCommissions)){
            for(MpCommission mpCommission:mpCommissions){
                long mpId=mpCommission.getMpId();
                BusinessProduct businessProduct=map.get(mpId);
                if(businessProduct!=null){
                    businessProduct.setCommodityCommission(mpCommission.getTotalMoney().doubleValue());
                }
            }
        }

    }
}
