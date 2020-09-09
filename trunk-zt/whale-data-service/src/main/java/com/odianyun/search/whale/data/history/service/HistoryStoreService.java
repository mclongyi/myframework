package com.odianyun.search.whale.data.history.service;

import com.odianyun.search.whale.data.model.history.LogWord;

public interface HistoryStoreService {

	void store(LogWord logWord);
	
}
