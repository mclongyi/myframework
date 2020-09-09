package com.odianyun.search.whale.data.suggest.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.data.model.suggest.SuggestWord;
import com.odianyun.search.whale.data.suggest.dao.SuggestWordDao;
import com.odianyun.search.whale.data.suggest.service.SuggestWordService;

public class SuggestWordServiceImpl implements SuggestWordService {

	@Autowired
	SuggestWordDao suggestWordDao;
	
	@Override
	public void rollbackSuggestWord() throws Exception {
		// TODO Auto-generated method stub
		suggestWordDao.cleanSuggestWords();
		suggestWordDao.rollbackSuggestWord();
	}

	@Override
	public Integer countSuggestWord() throws Exception {
		// TODO Auto-generated method stub
		return suggestWordDao.countSuggestWords();
	}

	@Override
	public void insertOrUpdateSuggestWords(
			List<SuggestWord> merchantProductSearches) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public List<SuggestWord> getSuggestWordsWithPage(int pageNo, int pageSize)
			throws Exception {
		// TODO Auto-generated method stub
		return suggestWordDao.querySuggestWordsWithPage(pageNo, pageSize);
	}

	@Override
	public void reloadDB() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cleanTemp() throws Exception {
		// TODO Auto-generated method stub
		suggestWordDao.cleanTempSuggestWords();
	}

	@Override
	public void backupTable() throws Exception {
		// TODO Auto-generated method stub
		suggestWordDao.cleanBackupSuggestWords();
		suggestWordDao.backupSuggestWords();
	}

	@Override
	public void switchTable() throws Exception {
		// TODO Auto-generated method stub
		suggestWordDao.cleanSuggestWords();
		suggestWordDao.copyToSuggestWords();
	}

	@Override
	public boolean validation() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
