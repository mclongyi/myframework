package com.odianyun.search.whale.data.service;

import java.util.List;
import java.util.Map;

import com.odianyun.search.whale.data.model.MerchantSeriesAttribute;

public interface MerchantSeriesService {

	
	/**
	 * 
	 * @param merchantSeriesIds  系列品IDs
	 * @return main_merchant_product_Id--->seriesId
	 */
	public Map<Long,Long> getMerchantSeriesById(List<Long> merchantSeriesIds, int companyId) throws Exception;
	
	/**
	 * 
	 * @param merchantSeriesIds  系列品IDs
	 * @return seriesId ---> main_merchant_product_Id
	 */
	public Map<Long,Long> getMerchantSeriesByIdRevert(List<Long> merchantSeriesIds, int companyId) throws Exception;
	
	
	//通过merchantSeriesId获取对应的系列属性
	public Map<Long,List<MerchantSeriesAttribute>> getMerchantProductAttrValues(List<Long> merchantSeriesIds, int companyId) throws Exception;

	/**
	 *
	 * @param merchantSeriesIds
	 * @param companyId
	 * @return  seriesId ---> List<MerchantProductId>  返回该系列对应的所有的mpId
	 * @throws Exception
     */
	public Map<Long,List<Long>> getSeriesId2MerchantProductIds(List<Long> merchantSeriesIds, int companyId)throws Exception;
	
	/**
	 *
	 * @param merchantSeriesIds
	 * @param companyId
	 * @return  List<MerchantProductId>  返回该系列对应的所有的mpId
	 * @throws Exception
    */
	public List<Long> getSeriesMerchantProductIds(List<Long> merchantSeriesIds, int companyId)throws Exception;

}
