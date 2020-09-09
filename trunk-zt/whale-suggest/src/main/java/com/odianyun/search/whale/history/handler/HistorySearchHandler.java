package com.odianyun.search.whale.history.handler;

import java.util.List;

import com.odianyun.search.whale.index.api.model.req.HistoryType;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.common.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.api.model.req.HistoryReadRequest;
import com.odianyun.search.whale.api.model.resp.HistoryResponse;
import com.odianyun.search.whale.index.api.model.req.HistoryResult;
import com.odianyun.search.whale.history.redis.UserHistoryResdis;

public class HistorySearchHandler {

	@Autowired
	UserHistoryResdis userHistoryResdis;
	
	public HistoryResponse handle(HistoryReadRequest request) {
		// TODO Auto-generated method stub

		if(request == null || StringUtils.isBlank(request.getUserId())){
			return null;
		}
		if (request.getType() != null && request.getType() == HistoryType.MERCHANT && request.getMerchantId() == null) {
			return null;
		}
		if (request.getMerchantId() == null) {
			request.setMerchantId(-1L);
		}
		HistoryResponse historyResponse = new HistoryResponse();

		List<HistoryResult> historyResult = userHistoryResdis.getUserHistory(request);
		if(CollectionUtils.isNotEmpty(historyResult)){
			if(historyResult.size() > request.getCount()){
				historyResult = historyResult.subList(0, request.getCount());
			}
			historyResponse.setHistoryResult(historyResult);
		}
		return historyResponse;
	}

}
