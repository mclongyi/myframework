package com.odianyun.search.whale.data.history.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.data.history.dao.HistoryStoreDao;
import com.odianyun.search.whale.data.history.service.HistoryStoreService;
import com.odianyun.search.whale.data.model.history.LogWord;

public class HistoryStoreServiceImpl implements HistoryStoreService {

	@Autowired
	HistoryStoreDao historyStoreDao;
	
	@Override
	public void store(LogWord logWord) {
		
		historyStoreDao.store(logWord);
	}

}
