package com.odianyun.search.whale.data.suggest.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.data.dao.AreaDao;
import com.odianyun.search.whale.data.model.Area;
import com.odianyun.search.whale.data.suggest.service.AreaSuggestWordService;

public class AreaSuggestWordServiceImpl implements AreaSuggestWordService {
	
	@Autowired
	AreaDao areaDao;

	@Override
	public List<Area> getAreaSuggestWords(int companyId) throws Exception {
		List<Area> areaList = areaDao.queryAllAreas(companyId);
		return areaList;
	}

	@Override
	public List<Area> getAreaSuggestWords() throws Exception {
		return areaDao.queryAllAreas();
	}


}
