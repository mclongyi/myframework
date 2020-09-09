package com.odianyun.search.whale.data.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.data.dao.MerchantSeriesDao;
import com.odianyun.search.whale.data.model.AttributeValue;
import com.odianyun.search.whale.data.model.MerchantSeries;
import com.odianyun.search.whale.data.model.MerchantSeriesAttribute;
import com.odianyun.search.whale.data.service.MerchantSeriesService;

public class MerchantSeriesServiceImpl implements MerchantSeriesService {

	@Autowired
	MerchantSeriesDao seriesDao;

	@Override
	public Map<Long, Long> getMerchantSeriesById(List<Long> merchantSeriesIds, int companyId) throws Exception{
		Map<Long,Long> seriesMap =new HashMap<Long,Long>();
		List<MerchantSeries> merchantSeriesList=seriesDao.querySeriesList(merchantSeriesIds, companyId);
		if(CollectionUtils.isNotEmpty(merchantSeriesList)){
			for(MerchantSeries series:merchantSeriesList)
				seriesMap.put(series.getMain_merchant_product_id(),series.getId());
		}
		return seriesMap;
	}

	
	@Override
	public Map<Long, List<MerchantSeriesAttribute>> getMerchantProductAttrValues(
			List<Long> merchantSeriesIds, int companyId) throws Exception{
		Map<Long, List<MerchantSeriesAttribute>> seriesAttributes=new HashMap<Long, List<MerchantSeriesAttribute>>();
		// AttributeValue对象中只有merchantSeriesId 和 attrNameId  
		List<MerchantSeriesAttribute> attributeValues=seriesDao.querySeriesAttributeValue(merchantSeriesIds,companyId);
		if(CollectionUtils.isNotEmpty(attributeValues)){
			for(MerchantSeriesAttribute attributeValue:attributeValues){
				List<MerchantSeriesAttribute> attrValues=seriesAttributes.get(attributeValue.getMerchantSeriesId());
				if(attrValues==null){
					attrValues=new LinkedList<MerchantSeriesAttribute>();
					seriesAttributes.put(attributeValue.getMerchantSeriesId(), attrValues);
				}
				attrValues.add(attributeValue);
			}
		}
		return seriesAttributes;
	}

	@Override
	public Map<Long, List<Long>> getSeriesId2MerchantProductIds(List<Long> merchantSeriesIds, int companyId) throws Exception {
		Map<Long,List<Long>> seriesId2MerchantProductIds=new HashMap<Long,List<Long>>();
		Map<Long,Long> mpId2SeriesId=seriesDao.queryMpId2SeriesId(merchantSeriesIds, companyId);
        if(mpId2SeriesId!=null && mpId2SeriesId.size()>0){
             for(Entry<Long,Long> entry:mpId2SeriesId.entrySet()){
				 Long mpId=entry.getKey();
				 Long seriesId=entry.getValue();
				 List<Long> mpIds=seriesId2MerchantProductIds.get(seriesId);
				 if(mpIds==null){
					 mpIds=new ArrayList<Long>();
					 seriesId2MerchantProductIds.put(seriesId,mpIds);
				 }
				 mpIds.add(mpId);
			 }
		}
		return seriesId2MerchantProductIds;
	}


	@Override
	public List<Long> getSeriesMerchantProductIds(List<Long> merchantSeriesIds, int companyId) throws Exception {
		List<Long> merchantProductIds = new ArrayList<>();
		Map<Long,Long> mpId2SeriesId = seriesDao.queryMpId2SeriesIdAll(merchantSeriesIds, companyId);
        if(mpId2SeriesId != null && mpId2SeriesId.size() > 0){
        	merchantProductIds.addAll(mpId2SeriesId.keySet());
        }

		return merchantProductIds;
	}


	@Override
	public Map<Long, Long> getMerchantSeriesByIdRevert(List<Long> merchantSeriesIds, int companyId) throws Exception {
		Map<Long,Long> seriesMap =new HashMap<Long,Long>();
		List<MerchantSeries> merchantSeriesList=seriesDao.querySeriesList(merchantSeriesIds, companyId);
		if(CollectionUtils.isNotEmpty(merchantSeriesList)){
			for(MerchantSeries series:merchantSeriesList)
				seriesMap.put(series.getId(),series.getMain_merchant_product_id());
		}
		return seriesMap;
	}

}
