package com.odianyun.search.whale.data.suggest.service;

import java.util.List;
import java.util.Map;

import com.odianyun.search.whale.data.model.suggest.KeyWord;
import com.odianyun.search.whale.data.model.suggest.WordWithCompany;

public interface HotWordService {

	List<WordWithCompany> getHotWordsWithPage(int pageNo, int pageSize,int companyId) throws Exception;

	Map<String,Integer> getHotWords(int companyId) throws Exception;

	Map<KeyWord,KeyWord> getHotKeywords(int companyId) throws Exception;

	Map<KeyWord,KeyWord> getPointHotKeywords(Integer companyId) throws Exception;
}
