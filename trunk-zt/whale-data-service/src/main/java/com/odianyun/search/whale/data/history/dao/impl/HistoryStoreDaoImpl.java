package com.odianyun.search.whale.data.history.dao.impl;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.odianyun.search.whale.data.history.dao.HistoryStoreDao;
import com.odianyun.search.whale.data.model.history.LogWord;

public class HistoryStoreDaoImpl extends SqlMapClientDaoSupport  implements HistoryStoreDao {

	@Override
	public void store(LogWord logWord) {
		// TODO Auto-generated method stub
		getSqlMapClientTemplate().update("updateOrInsertLogWord", logWord);
	}

}
