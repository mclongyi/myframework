package com.odianyun.search.whale.data.suggest.dao.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.odianyun.search.whale.data.suggest.dao.SuggestWordDao;
import com.ibatis.sqlmap.client.SqlMapExecutor;
import com.odianyun.search.whale.data.model.suggest.SuggestWord;

public class SuggestWordDaoImpl extends SqlMapClientDaoSupport implements SuggestWordDao {

	@Override
	public List<SuggestWord> querySuggestWordsWithPage(int pageNum, int pageSize) {
		// TODO Auto-generated method stub
		Map<String,Object> params = new HashMap<>();
		
		int startIndex = (pageNum - 1) * pageSize;
        params.put("startIndex", startIndex);
        params.put("pageSize", pageSize);
        
		return getSqlMapClientTemplate().queryForList("querySuggestWordsWithPage", params);
	}

	@Override
	public Integer countSuggestWords() {
		// TODO Auto-generated method stub
		return (Integer) getSqlMapClientTemplate().queryForObject("countSuggestWords");
	}

	@Override
	public void insertSuggestWords(List<SuggestWord> words) {
		// TODO Auto-generated method stub

	}

	@Override
	public void cleanSuggestWords() {
		// TODO Auto-generated method stub
		getSqlMapClientTemplate().update("cleanSuggestWords");
	}

	@Override
	public Integer countSuggestWordTemp() {
		// TODO Auto-generated method stub
		return (Integer) getSqlMapClientTemplate().queryForObject("countSuggestWordTemp");
	}

	@Override
	public void backupSuggestWords() {
		// TODO Auto-generated method stub
		getSqlMapClientTemplate().insert("backupSuggestWords");
	}

	@Override
	public void storeToTemp(final List<SuggestWord> list) throws Exception {
		// TODO Auto-generated method stub
		getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			
			@Override
			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				executor.startBatch();
	               int i = 1;
	               for (SuggestWord suggestWord : list) {
	                   executor.insert("storeToTemp", suggestWord);
	                   if (i % 500 == 0) {
	                       // executeBatch会将inBatch置为false
	                       executor.executeBatch();
	                       // 需要再启动一次
	                       executor.startBatch();
	                   }
	                   i++;
	               }
	               executor.executeBatch();
				return null;
			}
		});
	}

	@Override
	public void cleanTempSuggestWords() throws Exception {
		// TODO Auto-generated method stub
		getSqlMapClientTemplate().update("cleanTempSuggestWords");
	}

	@Override
	public void copyToSuggestWords() throws Exception {
		// TODO Auto-generated method stub
		getSqlMapClientTemplate().insert("copyToSuggestWords");
	}

	@Override
	public void cleanBackupSuggestWords() throws Exception {
		// TODO Auto-generated method stub
		getSqlMapClientTemplate().update("cleanBackupSuggestWords");
	}

	@Override
	public void rollbackSuggestWord() throws Exception {
		// TODO Auto-generated method stub
		getSqlMapClientTemplate().insert("rollbackSuggestWord");
	}

}
