package com.odianyun.search.whale.index.history.server;

import com.odianyun.soa.InputDTO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.odianyun.search.whale.index.api.model.req.HistoryWriteRequest;
import com.odianyun.search.whale.index.api.service.HistoryLogService;
import com.odianyun.search.whale.index.history.handler.HistoryLogHandler;

public class HistoryLogServer implements HistoryLogService {
	static Logger logger = Logger.getLogger(HistoryLogServer.class);
	@Autowired
	HistoryLogHandler historyLogHandler;
	
	@Override
	public void logSearchHistory(HistoryWriteRequest request)  {
		historyLogHandler.handle(request);
	}

	@Override
	public void logSearchHistoryStandard(InputDTO<HistoryWriteRequest> inputDTO) {
		logger.info("soa 调用 入参:"+inputDTO);
		historyLogHandler.handle(inputDTO.getData());
	}

}
