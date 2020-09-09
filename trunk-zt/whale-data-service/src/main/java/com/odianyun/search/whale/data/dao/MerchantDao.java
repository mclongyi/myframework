package com.odianyun.search.whale.data.dao;

import java.util.List;

import com.odianyun.search.whale.data.model.Merchant;
import com.odianyun.search.whale.data.model.MerchantBelongArea;
import com.odianyun.search.whale.data.model.Shop;
import com.odianyun.search.whale.data.model.geo.MerchantStoreCalendar;
import com.odianyun.search.whale.data.model.geo.MerchantStoreCalendarItem;

public interface MerchantDao{
	
	/**
	 * 读取所有的商家数据,适用于全量索引
	 * @return
	 * @throws Exception
	 */
	public List<Merchant> queryAllMerchant(int companyId) throws Exception;
	
	/**
	 * 分页读取商家数据
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public List<Merchant> queryMerchantsWithPage(int pageNo,int pageSize, int companyId) throws Exception;
	
	/**
	 * 根据商家ID列表获取相应的商家数据，适用于实时索引
	 * @param merchantIds
	 * @return
	 * @throws Exception
	 */
	public List<Merchant> getMerchants(List<Long> merchantIds, int companyId) throws Exception;
	
	/**
	 * 读取所有的店铺数据,适用于全量索引
	 * @return
	 * @throws Exception
	 */
	public List<Shop> queryAllShops(int companyId) throws Exception;
	
	/**
	 * 根据商家ID列表获取对应的店铺信息，适用于实时索引
	 * @param merchantIds
	 * @return
	 * @throws Exception
	 */
	public List<Shop> getShopsByMerchantIds(List<Long> merchantIds, int companyId) throws Exception;
	
	public List<MerchantBelongArea> queryAllBelongAreas(int companyId) throws Exception;
	
	public List<MerchantBelongArea> getBelongAreasByMerchantIds(List<Long> merchantIds, int companyId) throws Exception;

	public List<Merchant> getMerchantsByIds(List<Long> merchantIds, int companyId) throws Exception;

	/**
	 * 根据companyId 和周几获取门店营业时间配置
	 * @param weekIndex
	 * @param companyId
	 * @return
	 * @throws Exception
     */
	List<MerchantStoreCalendar> queryMerchantStoreCalendars(int weekIndex, int companyId) throws Exception;

	/**
	 * 根据companyId 获取门店营业时间范围
	 * @param companyId
	 * @return
	 * @throws Exception
     */
	List<MerchantStoreCalendarItem> queryMerchantStoreCalendarItems(int companyId) throws Exception;

	/**
	 * 根据companyId 获取门店营业时间范围
	 * @param companyId
	 * @return
	 * @throws Exception
	 */
	List<MerchantStoreCalendarItem> queryMerchantStoreCalendarItemsByIds(List<Long> merchantIds, int companyId) throws Exception;
	/**
	 * 根据companyId 和周几获取门店营业时间配置
	 * @param weekIndex
	 * @param companyId
	 * @return
	 * @throws Exception
	 */
	List<MerchantStoreCalendar> queryMerchantStoreCalendarsByIds(List<Long> merchantIds, int weekIndex, int companyId) throws Exception;
}
