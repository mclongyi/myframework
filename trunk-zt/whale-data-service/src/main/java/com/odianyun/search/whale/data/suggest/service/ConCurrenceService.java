package com.odianyun.search.whale.data.suggest.service;

import java.util.List;
import java.util.Map;

import com.odianyun.search.whale.data.model.suggest.KeyWord;
import com.odianyun.search.whale.data.model.suggest.WordWithCompany;

public interface ConCurrenceService {
	/**
	 * 统计共现热词
	 * @throws Exception
	 */
	public Map<String,Integer> conCurrenceWordCalc(int companyId) throws Exception;

	public Map<KeyWord,KeyWord> concurrenceKeywordCalc(int companyId) throws Exception;

	public Map<KeyWord,KeyWord> concurrencePointKeywordCalc(Integer companyId) throws Exception;

//	public List<WordWithCompany> getConCurrenceWordsWithPage(int pageNo, int pageSize,int companyId) throws Exception;
}