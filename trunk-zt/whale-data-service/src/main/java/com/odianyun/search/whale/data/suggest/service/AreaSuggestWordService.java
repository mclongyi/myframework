package com.odianyun.search.whale.data.suggest.service;

import java.util.List;

import com.odianyun.search.whale.data.model.Area;

public interface AreaSuggestWordService {

	List<Area> getAreaSuggestWords(int companyId) throws Exception;

	List<Area> getAreaSuggestWords() throws Exception;

}
