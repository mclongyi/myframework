package com.odianyun.search.whale.data;

import com.odianyun.search.whale.common.remote.AgentRemoteService;
import com.odianyun.search.whale.common.remote.PromotionRemoteService;
import com.odianyun.search.whale.common.remote.dto.*;
import com.odianyun.soa.CommonInputDTO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RemoteSOAServiceTest {
    public static void main(String[] args) throws Exception {
        System.setProperty("global.config.path", "/Users/hs/Desktop/lyf/env-edu");

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext-common.xml");

//        CompanySqlDaoImpl dao = (CompanySqlDaoImpl) applicationContext.getBean("companySqlDao");

        PromotionRemoteService promotionRemoteService = (PromotionRemoteService) applicationContext.getBean("promotionRemoteService");
        AgentRemoteService agentRemoteService = (AgentRemoteService) applicationContext.getBean("agentRemoteService");

        CommonInputDTO<MPPromotionListInputDTO> input = new CommonInputDTO<MPPromotionListInputDTO>();
        MPPromotionListInputDTO data = new MPPromotionListInputDTO();
        List<MPPromotionInputDTO> requestList = new ArrayList<>();
        data.setMpPromotionListInput(requestList);
        input.setData(data);

        MPPromotionInputDTO mp = new MPPromotionInputDTO();
        mp.setMpId(1006020801001460L);
        mp.setNum(1);
        mp.setPrice(new BigDecimal("5.00"));
        MPPromotionInputDTO mp2 = new MPPromotionInputDTO();
        mp2.setMpId(1006020801001378L);
        mp2.setNum(1);
        mp2.setPrice(new BigDecimal("40.00"));

        requestList.add(mp);
        requestList.add(mp2);

        input.setCompanyId(30L);
        MPPromotionListOutputDTO mpPromotionListOutputDTO = promotionRemoteService.batchGetMPPromotionsBaseInfo(input);
        System.out.println(mpPromotionListOutputDTO);

//        CommissionReadServiceClient instance = CommissionReadServiceClient.getInstance();
//        OutputDTO<List<com.odianyun.agent.model.dto.MpCommission>> outputDTO = new OutputDTO<>();
//        InputDTO<com.odianyun.agent.model.request.CommissionType> inputDTO = new InputDTO<>();
//        com.odianyun.agent.model.request.CommissionType type = new CommissionType();
//        type.setMpIds(Arrays.asList(1004053602000018L, 1006020801002706L));
//        inputDTO.setData(type);
//        inputDTO.setCompanyId(30L);
//        try {
//            outputDTO = instance.queryOrderMpTotalMoney(inputDTO);
//        }catch (Exception e){
//            e.printStackTrace();
//        }

        List<ValidateMpResultDTO> validateMpResultDTOS = agentRemoteService.validateMpInCommission(Arrays.asList(1004053602000018L, 1006020801002706L));
        List<MpCommission> mpCommissions = agentRemoteService.queryOrderMpTotalMoney(Arrays.asList(1004053602000018L, 1006020801002706L));
        System.out.println(validateMpResultDTOS);
        System.out.println(mpCommissions);
    }
}
