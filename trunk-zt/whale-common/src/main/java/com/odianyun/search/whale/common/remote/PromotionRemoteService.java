package com.odianyun.search.whale.common.remote;

import com.odianyun.search.whale.common.remote.dto.MPPromotionListInputDTO;
import com.odianyun.search.whale.common.remote.dto.MPPromotionListOutputDTO;
import com.odianyun.soa.InputDTO;
import com.odianyun.soa.OutputDTO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hs
 * @date 2018/8/27.
 */
@Service("promotionRemoteService")
public class PromotionRemoteService {

    Logger logger = Logger.getLogger(getClass());

    @Autowired
    private RemoteSoaService remoteSoaService;

    public MPPromotionListOutputDTO batchGetMPPromotionsBaseInfo(InputDTO<MPPromotionListInputDTO> inputDTO) throws Exception{
//        logger.info("SOA 调用开始 promotion_batchGetMPPromotions...."+inputDTO);
        inputDTO.setCompanyId(30L);
        OutputDTO<MPPromotionListOutputDTO> outputDTO = remoteSoaService.call(RemoteSoaServiceEnum.promotion_batchGetMPPromotions, inputDTO);
//        logger.info("SOA 调用结束 promotion_batchGetMPPromotions....");
        return outputDTO.getData();
    }
}
