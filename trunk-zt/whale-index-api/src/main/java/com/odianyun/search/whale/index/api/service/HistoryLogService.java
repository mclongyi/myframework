package com.odianyun.search.whale.index.api.service;

import com.odianyun.search.whale.index.api.model.req.HistoryWriteRequest;
import com.odianyun.soa.InputDTO;

public interface HistoryLogService {
	
	/**
	 * 记录搜索记录
	 * @param userId
	 * @param keyword
	 * @param type
	 * @throws SearchException
	 */
	@Deprecated
	public void logSearchHistory(HistoryWriteRequest request);

	void logSearchHistoryStandard(InputDTO<HistoryWriteRequest> inputDTO);

}