package com.odianyun.search.whale.data.dao;

import java.util.List;

import com.odianyun.search.whale.data.model.Area;

public interface AreaDao {

	public List<Area> queryAllAreas(int companyId) throws Exception;

	List<Area> queryAllAreas() throws Exception;

	public List<Area> queryAreasByIds(List<Long> areaIds, int companyId) throws Exception;
	
	public List<Area> queryAreasByCodes(List<Long> codes, int companyId) throws Exception;
}
