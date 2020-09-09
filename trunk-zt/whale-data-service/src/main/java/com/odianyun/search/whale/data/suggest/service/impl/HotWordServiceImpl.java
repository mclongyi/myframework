package com.odianyun.search.whale.data.suggest.service.impl;

import java.util.List;
import java.util.Map;

import com.odianyun.search.whale.data.model.suggest.KeyWord;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.data.model.suggest.WordWithCompany;
import com.odianyun.search.whale.data.suggest.service.ConCurrenceService;
import com.odianyun.search.whale.data.suggest.service.HotWordService;

public class HotWordServiceImpl implements HotWordService {

	@Autowired
	ConCurrenceService conCurrenceService;
	
	@Override
	public List<WordWithCompany> getHotWordsWithPage(int pageNo, int pageSize,int companyId) throws Exception {
//		List<WordWithCompany> wordList = conCurrenceService.getConCurrenceWordsWithPage(pageNo,pageSize,companyId);
//		
//		return wordList;
		return null;
	}

	@Override
	public Map<String,Integer> getHotWords(int companyId) throws Exception {
		return conCurrenceService.conCurrenceWordCalc(companyId);

	}

	@Override
	public Map<KeyWord, KeyWord> getHotKeywords(int companyId) throws Exception {
		return conCurrenceService.concurrenceKeywordCalc(companyId);
	}

	@Override
	public Map<KeyWord, KeyWord> getPointHotKeywords(Integer companyId) throws Exception {
		return conCurrenceService.concurrencePointKeywordCalc(companyId);
	}

}
