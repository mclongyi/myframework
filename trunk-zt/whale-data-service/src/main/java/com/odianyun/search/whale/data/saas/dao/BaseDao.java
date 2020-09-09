package com.odianyun.search.whale.data.saas.dao;

import java.util.List;
import java.util.Map;

public interface BaseDao {

	public List queryForList(String sql);
	
	public List queryForList(String sql,Object paramObject);
	
	public Map queryForObject(String sql);
	
	public Map queryForObject(String sql,Object paramObject);
	
}
