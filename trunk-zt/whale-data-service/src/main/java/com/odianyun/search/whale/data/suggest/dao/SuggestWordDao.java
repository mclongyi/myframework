package com.odianyun.search.whale.data.suggest.dao;

import java.util.List;

import com.odianyun.search.whale.data.model.suggest.SuggestWord;

public interface SuggestWordDao {
	
	public List<SuggestWord> querySuggestWordsWithPage(int pageNum,int pageSize);
	
	public Integer countSuggestWords();
	
	public void insertSuggestWords(List<SuggestWord> words);
	
	public void cleanSuggestWords();
    
	public Integer countSuggestWordTemp();
	
	public void backupSuggestWords();

	public void storeToTemp(List<SuggestWord> list) throws Exception;

	public void cleanTempSuggestWords() throws Exception;

	public void copyToSuggestWords() throws Exception;

	public void cleanBackupSuggestWords() throws Exception;

	public void rollbackSuggestWord() throws Exception;

}
