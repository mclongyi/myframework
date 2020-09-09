package com.odianyun.search.whale.data.suggest.service;

import java.util.List;

import com.odianyun.search.whale.data.model.suggest.SuggestWord;

public interface SuggestWordService {

    public void rollbackSuggestWord() throws Exception;
	
	public Integer countSuggestWord() throws Exception;
	
	public void insertOrUpdateSuggestWords(List<SuggestWord> merchantProductSearches) throws Exception;

	public List<SuggestWord> getSuggestWordsWithPage(int pageNo, int pageSize) throws Exception;

	public void reloadDB() throws Exception;

	public void cleanTemp() throws Exception;

	public void backupTable() throws Exception;

	public void switchTable() throws Exception;

	public boolean validation() throws Exception;
}
