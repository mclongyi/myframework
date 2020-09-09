package com.odianyun.search.whale.api.service;


import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.req.HistoryCleanRequest;
import com.odianyun.search.whale.api.model.req.HistoryReadRequest;
import com.odianyun.search.whale.api.model.resp.HistoryResponse;
import com.odianyun.soa.InputDTO;
import com.odianyun.soa.OutputDTO;
import com.odianyun.soa.SoaUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class HistoryCacheService implements HistoryService {

    private Logger logger = Logger.getLogger(this.getClass());

    HistoryService historyService;

    public HistoryCacheService(HistoryService historyService) {
        this.historyService = historyService;
    }

    @Override
    public HistoryResponse autoSearchHistory(HistoryReadRequest request) throws SearchException {
        // TODO Auto-generated method stub
        String userId = request.getUserId();
        if (StringUtils.isBlank(userId)) {
            throw new SearchException("userId cann't be null !!!");
        }
        request.setUserId(userId.trim());
        return historyService.autoSearchHistory(request);
    }

    @Override
    public void cleanSearchHistory(HistoryCleanRequest request) throws SearchException {
        // TODO Auto-generated method stub
        String userId = request.getUserId();
        if (StringUtils.isBlank(userId)) {
            throw new SearchException("userId cann't be null !!!");
        }
        request.setUserId(userId.trim());
        historyService.cleanSearchHistory(request);
    }

    //------------------------------------标准化改造--------------------------------------------------

    @Override
    public OutputDTO<HistoryResponse> autoSearchHistoryStandard(InputDTO<HistoryReadRequest> inputDTO) throws SearchException {
//        logger.info("soa 调用开始......"+inputDTO.toString());
        HistoryResponse historyResponse = null;
        try {
             historyResponse = autoSearchHistory(inputDTO.getData());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("autoSearchHistoryStandard调用失败,"+inputDTO.toString()+e);
        }
//        logger.info("soa 调用结束......"+inputDTO.toString());
        return SoaUtil.resultSucess(historyResponse);
    }

    @Override
    public void cleanSearchHistoryStandard(InputDTO<HistoryCleanRequest> inputDTO) throws SearchException {
//        logger.info("soa 调用入参......"+inputDTO.toString());
        try {
            cleanSearchHistory(inputDTO.getData());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("autoSearchHistoryStandard调用失败,"+inputDTO.toString()+e);
        }
//        logger.info("soa 调用结束......");
    }

}
