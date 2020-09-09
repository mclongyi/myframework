package com.odianyun.search.whale.history.server;

import com.odianyun.search.whale.index.api.model.req.HistoryType;
import com.odianyun.soa.InputDTO;
import com.odianyun.soa.OutputDTO;
import com.odianyun.soa.SoaUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.req.HistoryCleanRequest;
import com.odianyun.search.whale.api.model.req.HistoryReadRequest;
import com.odianyun.search.whale.api.model.resp.HistoryResponse;
import com.odianyun.search.whale.api.service.HistoryService;
import com.odianyun.search.whale.history.handler.HistoryCleanHandler;
import com.odianyun.search.whale.history.handler.HistorySearchHandler;

public class HistoryServer implements HistoryService {

	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	HistorySearchHandler historySearchHandler;
	
	@Autowired
	HistoryCleanHandler historyCleanHandler;
	
	@Override
	public HistoryResponse autoSearchHistory(HistoryReadRequest request) throws SearchException {
		if(request.getMerchantId()!=null && request.getMerchantId()!=0){
			request.setType(HistoryType.MERCHANT);
		}
		return historySearchHandler.handle(request);
	}

	@Override
	public void cleanSearchHistory(HistoryCleanRequest request) throws SearchException {
		historyCleanHandler.handle(request);
	}



	//------------------------------------标准化改造--------------------------------------------------

	@Override
	public OutputDTO<HistoryResponse> autoSearchHistoryStandard(InputDTO<HistoryReadRequest> inputDTO) throws SearchException {
		logger.info("soa 调用开始......"+inputDTO.toString());
		HistoryResponse historyResponse = null;
		try {
			historyResponse = autoSearchHistory(inputDTO.getData());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("autoSearchHistoryStandard调用失败,"+inputDTO.toString()+e);
		}
		logger.info("soa 调用结束......"+historyResponse);
		return SoaUtil.resultSucess(historyResponse);
	}

	@Override
	public void cleanSearchHistoryStandard(InputDTO<HistoryCleanRequest> inputDTO) throws SearchException {
		logger.info("soa 调用入参......"+inputDTO.toString());
		try {
			cleanSearchHistory(inputDTO.getData());
		}catch (Exception e){
			e.printStackTrace();
			logger.error("autoSearchHistoryStandard调用失败,"+inputDTO.toString()+e);
		}
		logger.info("soa 调用结束......");
	}

}
