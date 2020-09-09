package com.odianyun.search.whale.data.history.dao;

import com.odianyun.search.whale.data.model.history.LogWord;

public interface HistoryStoreDao {

	void store(LogWord logWord);

}
