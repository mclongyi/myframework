package com.odianyun.search.whale.data.suggest.dao;

import java.util.List;
import java.util.Map;

import com.odianyun.search.whale.data.model.suggest.WordWithCompany;
import com.odianyun.search.whale.data.saas.model.DBType;

public interface ConCurrenceDao {

	List<WordWithCompany> getWordsWithPage(int companyId,String tableName, String column, long maxId, int pageSize) throws Exception;

//	void storeToTemp(Map<String, Integer> conCurrenceWordMap) throws Exception;

//	List<WordWithCompany> getConCurrenceWordsWithPage(int pageNo, int pageSize) throws Exception;

//	void cleanConCurrenceTemp() throws Exception;

//	void backupTable() throws Exception;

//	void switchTable() throws Exception;

}
