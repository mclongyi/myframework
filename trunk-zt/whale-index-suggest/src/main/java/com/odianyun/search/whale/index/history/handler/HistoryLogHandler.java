package com.odianyun.search.whale.index.history.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.odianyun.search.whale.index.api.model.req.HistoryType;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.api.model.req.HistoryReadRequest;
import com.odianyun.search.whale.data.history.service.HistoryStoreService;
import com.odianyun.search.whale.data.model.history.LogWord;
import com.odianyun.search.whale.index.api.model.req.HistoryResult;
import com.odianyun.search.whale.index.api.model.req.HistoryWriteRequest;
import com.odianyun.search.whale.index.history.redis.UserHistoryResdis;

public class HistoryLogHandler {

	static int MAXSIZE = 20;
	
	@Autowired
	UserHistoryResdis userHistoryResdis;
	
	@Autowired
	HistoryStoreService histroyStoreService;
	
	public void handle(HistoryWriteRequest request) {
		if(request == null || StringUtils.isBlank(request.getUserId()) ){
			return ;
		}
		persist(request);
		
	}

	private void persist(final HistoryWriteRequest request) {
		/**

		es.submit(new Runnable() {

			@Override
			public void run() {
				persistToMysql(request);
			}
		});
		 *
		 */

		persistToRedis(request);
		
	}

	private void persistToRedis(HistoryWriteRequest request) {
		HistoryReadRequest readRequest = new HistoryReadRequest(request.getCompanyId(), request.getUserId(), MAXSIZE);
		if (request.getMerchantId() != null && request.getMerchantId() != 0) {
			request.setType(HistoryType.MERCHANT);
			readRequest.setType(HistoryType.MERCHANT);
			readRequest.setMerchantId(request.getMerchantId());
		}else{
			request.setMerchantId(-1l);
			readRequest.setMerchantId(-1l);
			readRequest.setType(request.getType());
		}
		List<HistoryResult> historyResult = userHistoryResdis.getUserHistory(readRequest);
		if(historyResult == null){
			historyResult = new ArrayList<>();
			historyResult.add(createHistoryResult(request));
			userHistoryResdis.setUserHisttory(request, historyResult);
			return;
		}
		HistoryResult newHistoryResult = createHistoryResult(request);
		int index = historyResult.indexOf(newHistoryResult);
		if(index != -1){
			if(index == 0){
				return;
			}
			historyResult.remove(index);
		}
		if(historyResult.size() >= MAXSIZE){
			historyResult.remove(MAXSIZE-1);
		}
		historyResult.add(0, newHistoryResult);
		userHistoryResdis.setUserHisttory(request, historyResult);
	}

	private void persistToMysql(HistoryWriteRequest request) {
		LogWord logWord = new LogWord();
		BeanUtils.copyProperties(request, logWord);
		logWord.setType(request.getType().getCode());
		histroyStoreService.store(logWord);
	}

	private HistoryResult createHistoryResult(HistoryWriteRequest request) {
		HistoryResult historyResult = new HistoryResult();
		historyResult.setType(request.getType());
		historyResult.setWord(request.getKeyword());
		historyResult.setMerchantId(request.getMerchantId());
		return historyResult;
	}

}
