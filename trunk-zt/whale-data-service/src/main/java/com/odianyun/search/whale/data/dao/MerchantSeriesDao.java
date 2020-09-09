package com.odianyun.search.whale.data.dao;

import java.util.List;
import java.util.Map;

import com.odianyun.search.whale.data.model.MerchantSeries;
import com.odianyun.search.whale.data.model.MerchantSeriesAttribute;

public interface MerchantSeriesDao {

//	public List<MerchantSeries> queryAllSeries(int companyId) throws Exception;
	
	public List<MerchantSeries> querySeriesList(List<Long> merchantSeriesIdList, int companyId) throws Exception;
	
	public List<MerchantSeriesAttribute> querySeriesAttributeValue(List<Long> merchantSeriesIdList, int companyId) throws Exception;

	public Map<Long,Long>  queryMpId2SeriesId(List<Long> merchantSeriesIdList, int companyId) throws Exception;

	public Map<Long, Long> queryMpId2SeriesIdAll(List<Long> merchantSeriesIds, int companyId) throws Exception;
	
}
