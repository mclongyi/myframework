package com.odianyun.search.whale.common.remote;

import com.odianyun.search.whale.common.remote.dto.CommissionType;
import com.odianyun.search.whale.common.remote.dto.MpCommission;
import com.odianyun.search.whale.common.remote.dto.ValidateMpResultDTO;
import com.odianyun.soa.InputDTO;
import com.odianyun.soa.OutputDTO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hs
 * @date 2018/9/4.
 */
@Service("agentRemoteService")
public class AgentRemoteService {

     Logger logger = Logger.getLogger(getClass());

    @Autowired
    private RemoteSoaService remoteSoaService;

    public List<ValidateMpResultDTO> validateMpInCommission(List<Long> mpIds) throws SQLException {
//       logger.info("SOA 调用开始 agent_commissionReadService_validateMpInCommission...."+mpIds);
        OutputDTO<List<ValidateMpResultDTO>> outputDTO = new OutputDTO<>();
        if (mpIds==null || mpIds.isEmpty()){
            return outputDTO.getData();
        }

        InputDTO<List<Long>> inputDTO = new InputDTO<>();
        inputDTO.setData(mpIds);
        inputDTO.setCompanyId(30L);
        outputDTO = remoteSoaService.call(RemoteSoaServiceEnum.agent_commissionReadService_validateMpInCommission,inputDTO);
//        logger.info("SOA 调用结束 agent_commissionReadService_validateMpInCommission...."+outputDTO);
        return outputDTO.getData();
    }

    public List<MpCommission> queryOrderMpTotalMoney(List<Long> mpIds) throws SQLException {
//        logger.info("SOA 调用开始 agent_commissionReadService_queryOrderMpTotalMoney...."+mpIds);
        OutputDTO<List<MpCommission>> outputDTO = new OutputDTO<>();
        if (mpIds==null || mpIds.isEmpty()){
            return outputDTO.getData();
        }

        InputDTO<CommissionType> inputDTO = new InputDTO<>();
        CommissionType type = new CommissionType();
        type.setMpIds(mpIds);
        inputDTO.setData(type);
        inputDTO.setCompanyId(30L);
        outputDTO = remoteSoaService.call(RemoteSoaServiceEnum.agent_commissionReadService_queryOrderMpTotalMoney,inputDTO);

//        logger.info("SOA 调用结束 agent_commissionReadService_queryOrderMpTotalMoney...."+outputDTO);
        return outputDTO.getData();
    }


//    OutputDTO<List<ValidateMpResultDTO>> validateMpInCommission(InputDTO<List<Long>> var1);
//OutputDTO<List<MpCommission>> queryOrderMpTotalMoney(InputDTO<CommissionType> var1);

}
