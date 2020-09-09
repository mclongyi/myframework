package com.odianyun.search.whale.history.handler;

import java.util.List;

import com.odianyun.search.whale.index.api.model.req.HistoryType;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.common.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.api.model.req.HistoryCleanRequest;
import com.odianyun.search.whale.api.model.req.HistoryReadRequest;
import com.odianyun.search.whale.index.api.model.req.HistoryResult;
import com.odianyun.search.whale.index.api.model.req.HistoryWriteRequest;
import com.odianyun.search.whale.history.redis.UserHistoryResdis;

public class HistoryCleanHandler {

	static int MAXSIZE = 20;
	
	@Autowired
	UserHistoryResdis userHistoryResdis;

	public void handle(HistoryCleanRequest request) {
		if(request == null || StringUtils.isBlank(request.getUserId()) ){
			return ;
		}
		if (request.getType() != null && request.getType() == HistoryType.MERCHANT && request.getMerchantId() == null) {
			return;
		}
		if (request.getMerchantId() == null || request.getMerchantId() == 0) {
			request.setMerchantId(-1L);
		} else {
			request.setType(HistoryType.MERCHANT);
		}
		List<String> keywords =  request.getKeywordList();
		
		if(CollectionUtils.isEmpty(keywords)){
			
			userHistoryResdis.clean(request);
		}else{
			HistoryReadRequest readRequest = new HistoryReadRequest(request.getCompanyId(), request.getUserId(), request.getCount());
			List<HistoryResult> historyResult = userHistoryResdis.getUserHistory(readRequest);
			if(historyResult == null){
				return ;
			}
			for(String keyword : keywords){
				HistoryResult hResult = new HistoryResult();
				hResult.setType(request.getType());
				hResult.setWord(keyword);
				int index = historyResult.indexOf(hResult);
				if(index != -1){
					historyResult.remove(index);
				}
			}
			HistoryWriteRequest writeRetuest = new HistoryWriteRequest(request.getCompanyId(), request.getUserId(), null, request.getType());
			userHistoryResdis.setUserHisttory(writeRetuest, historyResult);
		}
	}

}
