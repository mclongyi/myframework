package com.odianyun.search.whale.data.saas.dao;

import java.util.List;

import com.odianyun.search.whale.data.saas.model.CompanySqlConfig;

public interface CompanySqlDao {
	
	public List<CompanySqlConfig> getCompanySqlWithPage(long maxId, int pageSize);
	
	public List<CompanySqlConfig> getAllCompanySql();
}
