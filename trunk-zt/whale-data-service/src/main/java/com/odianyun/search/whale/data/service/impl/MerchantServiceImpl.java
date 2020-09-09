package com.odianyun.search.whale.data.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.odianyun.search.whale.common.util.JsonUtil;
import com.odianyun.search.whale.data.model.geo.BusinessTime;
import com.odianyun.search.whale.data.model.geo.MerchantStoreCalendar;
import com.odianyun.search.whale.data.model.geo.MerchantStoreCalendarItem;
import com.odianyun.search.whale.data.model.geo.MerchantStoreCalendarType;
import com.odianyun.search.whale.data.service.ConfigService;
import org.apache.commons.collections.ArrayStack;
import org.apache.commons.collections.CollectionUtils;
//import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.data.dao.MerchantDao;
import com.odianyun.search.whale.data.model.Merchant;
import com.odianyun.search.whale.data.model.MerchantBelongArea;
import com.odianyun.search.whale.data.model.Shop;
import com.odianyun.search.whale.data.service.AbstractCompanyDBService;
import com.odianyun.search.whale.data.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;

public class MerchantServiceImpl extends AbstractCompanyDBService implements MerchantService {


	static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

	MerchantDao merchantDao;

	@Autowired
	ConfigService configService;
	
	Map<Integer,MerchantCacheContext> merchantCacheContexts=new HashMap<Integer,MerchantCacheContext>();
	
	@Override
	public Map<Long, Merchant> getMerchantsByIds(List<Long> merchantIds,int companyId)
			throws Exception {
		Map<Long, Merchant> ret =new HashMap<Long, Merchant>();
		List<Merchant> merchants = merchantDao.getMerchantsByIds(merchantIds,companyId);
		if(merchantIds!=null && CollectionUtils.isNotEmpty(merchantIds)){
			for(Merchant merchant: merchants){
				ret.put(merchant.getId(),merchant);
			}
		}
		/*MerchantCacheContext merchantCacheContext=merchantCacheContexts.get(companyId);
		
		if(merchantCacheContext != null) {
			if(CollectionUtils.isNotEmpty(merchantIds)){
				for(Long merchantId : merchantIds){
					Merchant merchant=merchantCacheContext.merchantMap.get(merchantId);
					if(merchant!=null){
						ret.put(merchantId, merchant);
					}	
				}
			}
		}*/
		
		return ret;
	}

	private void reloadMerchants(MerchantCacheContext merchantCacheContext,int companyId) throws Exception {
		List<Merchant> merchantlist = merchantDao.queryAllMerchant(companyId);
		Map<Long, Merchant> tempMap =new HashMap<Long, Merchant>();
		if(merchantlist!=null){
			for(Merchant mer:merchantlist){
				tempMap.put(mer.getId(), mer);
			}
		}		
		merchantCacheContext.merchantMap=tempMap;
	}

	@Override
	protected void tryReload(int companyId) throws Exception {
		MerchantCacheContext merchantCacheContext=new MerchantCacheContext();
		reloadShops(merchantCacheContext,companyId);
		reloadMerchants(merchantCacheContext,companyId);	
		reloadBelongAreas(merchantCacheContext,companyId);
		reloadBusinessTimes(merchantCacheContext,companyId);

		merchantCacheContexts.put(companyId, merchantCacheContext);
	}

	private void reloadBusinessTimes(MerchantCacheContext merchantCacheContext, int companyId) throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int weekIndex = cal.get(Calendar.DAY_OF_WEEK) - 1;
		List<BusinessTime> allDay = new ArrayList<>();
//		allDay.add(new BusinessTime(sdf.parse("00:00:00"),sdf.parse("23:59:59")));
		allDay.add(new BusinessTime("00:00:00","23:59:59"));

		List<BusinessTime> empty = new ArrayList<>();


		Map<Long,List<BusinessTime>> tempMap=new HashMap<Long,List<BusinessTime>>();

		Map<Long,List<BusinessTime>> itemMap=new HashMap<Long,List<BusinessTime>>();
		List<MerchantStoreCalendarItem> merchantStoreCalendarItems = merchantDao.queryMerchantStoreCalendarItems(companyId);
		if(CollectionUtils.isNotEmpty(merchantStoreCalendarItems)){
			for(MerchantStoreCalendarItem item : merchantStoreCalendarItems){
				Long merchantStoreCalendarId = item.getMerchantStoreCalendarId();
				BusinessTime businessTime = convert(item);
				List<BusinessTime> list = itemMap.get(merchantStoreCalendarId);
				if(CollectionUtils.isEmpty(list)){
					list = new ArrayList<>();
					itemMap.put(merchantStoreCalendarId,list);
				}
				list.add(businessTime);
			}
		}


		List<MerchantStoreCalendar> merchantStoreCalendars = merchantDao.queryMerchantStoreCalendars(weekIndex,companyId);

		if(CollectionUtils.isNotEmpty(merchantStoreCalendars)){
			for(MerchantStoreCalendar merchantStoreCalendar : merchantStoreCalendars){
				Integer type = merchantStoreCalendar.getType();
				Long merchantId = merchantStoreCalendar.getMerchantId();
				Long id = merchantStoreCalendar.getId();
				List<BusinessTime> list = null;
				if(MerchantStoreCalendarType.ALL_DAY.getCode().equals(type)){
					list = allDay;
				}else if(MerchantStoreCalendarType.RETIREE.getCode().equals(type)){
					list = empty;
				}else{
					list = itemMap.get(id);
				}
				tempMap.put(merchantId,list);
			}
		}

		merchantCacheContext.businessTimesMap=tempMap;
	}

	private BusinessTime convert(MerchantStoreCalendarItem item) throws Exception {
		Date start = sdf.parse(item.getBeginDate());
		Date end = sdf.parse(item.getEndDate());
		BusinessTime businessTime = new BusinessTime(item.getBeginDate(),item.getEndDate());
		return businessTime;
	}

	@Override
	public int getInterval() {
		return 30;
	}

	@Override
	public Merchant getMerchantById(Long merchantId,int companyId) throws Exception {
		MerchantCacheContext merchantCacheContext = merchantCacheContexts.get(companyId);
		if(merchantCacheContext == null) {
			return null;
		}
		
		return merchantCacheContext.merchantMap.get(merchantId);
	}

	@Override
	public Map<Long, String> getMerchantMapByIds(List<Long> merchantIds,int companyId)
			throws Exception {
		Map<Long, String> ret =new HashMap<Long,String>();
		MerchantCacheContext merchantCacheContext = merchantCacheContexts.get(companyId);
		if(merchantCacheContext != null) {
			Map<Long, Merchant> merchantMap=merchantCacheContext.merchantMap;
			if(CollectionUtils.isNotEmpty(merchantIds)){
				for(Long mId:merchantIds){
					Merchant m = merchantMap.get(mId);
					if(m!=null){
						ret.put(mId, m.getCompany_name());
					}	
				}
			}
		}
				
		return ret;
	}

	@Override
	public String getMerchantName(Long merchantId,int companyId) throws Exception {
		String merchantName=null;
		MerchantCacheContext merchantCacheContext = merchantCacheContexts.get(companyId);
		if(merchantCacheContext != null) {
			Merchant m =merchantCacheContext.merchantMap.get(merchantId);
			if(m!=null){
				merchantName=m.getCompany_name();
			}
		}
		return merchantName;
	}
	
	private void reloadShops(MerchantCacheContext merchantCacheContext,int companyId) throws Exception{
		Map<Long,Shop> tempMap = new HashMap<Long,Shop>();
		Map<String, Shop> tempShopMap = new HashMap<String, Shop>();
		// 从配置中获取店铺名称的杂音词
		String shopNoiseWordString = configService.get("shop_noise_word", companyId);
		List<String> shopNoiseWordList = JsonUtil.parseJsonToList(shopNoiseWordString, String.class);
		List<Shop> shoplist = merchantDao.queryAllShops(companyId);
		if(shoplist!=null){
			for(Shop shop:shoplist){
				tempMap.put(shop.getMerchant_id(), shop);
				if (shop.getName() != null && !shop.getName().trim().equals("")) {
					String shopName = shop.getName();
					tempShopMap.put(shopName, shop);
					if (shopNoiseWordList != null) {
						for (String shopNoiseWord : shopNoiseWordList) {
							if (shopName.contains(shopNoiseWord)) {
								shopName = shopName.replaceAll(shopNoiseWord, "");
							}
						}
					}
					tempShopMap.put(shopName, shop);
				}
			}
		}
		merchantCacheContext.shopMap=tempMap;
		merchantCacheContext.shopNameMap = tempShopMap;
	}

	@Override
	public Shop getShopByShopName (String shopName, int companyId) {
		MerchantCacheContext merchantCacheContext = merchantCacheContexts.get(companyId);
		Shop shop = null;
		if (merchantCacheContext != null) {
			Map<String, Shop> shopNameMap = merchantCacheContext.shopNameMap;
			if (shopName != null && !shopName.trim().equals("")) {
				shop = shopNameMap.get(shopName);
			}
		}
		return shop;
	}

	@Override
	public Map<Long, Shop> getShopMapByMerchantIds(List<Long> merchantIds,int companyId)
			throws Exception {
		MerchantCacheContext merchantCacheContext = merchantCacheContexts.get(companyId);
		Map<Long,Shop> ret =new HashMap<Long,Shop>();
		if(merchantCacheContext != null) {
			Map<Long, Shop> shopMap=merchantCacheContext.shopMap;
			if(CollectionUtils.isNotEmpty(merchantIds)){
				for(Long mid:merchantIds){
					Shop shop = shopMap.get(mid);
					if(shop!=null){
						ret.put(mid, shop);
					}
				}
			}
		}
		
		return ret;
	}

	@Override
	public Shop getShop(Long merchantId,int companyId) throws Exception {
		MerchantCacheContext merchantCacheContext=merchantCacheContexts.get(companyId);
		if(merchantCacheContext == null) {
			return null;
		}
		return merchantCacheContext.shopMap.get(merchantId);
	}

	private void reloadBelongAreas(MerchantCacheContext merchantCacheContext,int companyId) throws Exception{
		Map<Long,MerchantBelongArea> tempMap = new HashMap<Long,MerchantBelongArea>();
		List<MerchantBelongArea> areaList = merchantDao.queryAllBelongAreas(companyId);
		if(areaList!=null){
			for(MerchantBelongArea mbArea:areaList){
				tempMap.put(mbArea.getMerchant_id(), mbArea);
			}
		}
		merchantCacheContext.belongAreaMap=tempMap;
	}

	@Override
	public Map<Long, MerchantBelongArea> getBelongAreaByMerchantIds(
			List<Long> merchantIds,int companyId) throws Exception {
		MerchantCacheContext merchantCacheContext = merchantCacheContexts.get(companyId);
		
		Map<Long, MerchantBelongArea> ret = new HashMap<Long, MerchantBelongArea>();
		if(merchantCacheContext != null) {
			if(CollectionUtils.isNotEmpty(merchantIds)){
				Map<Long,MerchantBelongArea> belongAreaMap=merchantCacheContext.belongAreaMap;
				for(Long mId : merchantIds){
					MerchantBelongArea mbArea = belongAreaMap.get(mId);
					if(mbArea!=null){
						ret.put(mId, mbArea);
					}
				}
			}	
		}
		
		return ret;
	}

	@Override
	public MerchantBelongArea getBelongAreaByMerchantId(Long merchantId,int companyId)
			throws Exception {
		MerchantCacheContext merchantCacheContext = merchantCacheContexts.get(companyId);
		if(merchantCacheContext == null) {
			return null;
		}
		return merchantCacheContext.belongAreaMap.get(merchantId);
	}

	@Override
	public List<BusinessTime> getMerchantBusinessTimes(Long merchantId, int companyId) throws Exception {
		MerchantCacheContext merchantCacheContext = merchantCacheContexts.get(companyId);
		if(merchantCacheContext == null) {
			return null;
		}
		return merchantCacheContext.businessTimesMap.get(merchantId);
	}

	@Override
	public List<Merchant> getMerchantsWithPage(int pageNo, int pageSize,int companyId)
			throws Exception {
		return merchantDao.queryMerchantsWithPage(pageNo,pageSize,companyId);
	}

	
	public MerchantDao getMerchantDao() {
		return merchantDao;
	}

	public void setMerchantDao(MerchantDao merchantDao) {
		this.merchantDao = merchantDao;
	}
	
	
	private static class MerchantCacheContext{
		
		Map<Long, Merchant> merchantMap=new HashMap<Long, Merchant>();
		
		Map<Long, Shop> shopMap=new HashMap<Long, Shop>();
		
		Map<Long,MerchantBelongArea> belongAreaMap=new HashMap<Long,MerchantBelongArea>();

		Map<Long,List<BusinessTime>> businessTimesMap=new HashMap<Long,List<BusinessTime>>();

		Map<String, Shop> shopNameMap = new HashMap<String, Shop>();

	}

	@Override
	protected void tryReload(List<Long> merchantIds, int companyId) throws Exception {
		if(!merchantCacheContexts.containsKey(companyId)){
			merchantCacheContexts.put(companyId,new MerchantCacheContext());
		}
		reloadShopsByIds(merchantIds,companyId);
		reloadMerchantsByIds(merchantIds,companyId);
		reloadBusinessTimesByIds(merchantIds,companyId);
	}

	private void reloadMerchantsByIds(List<Long> merchantIds, int companyId) throws Exception {
		List<Merchant> merchantList = merchantDao.getMerchants(merchantIds,companyId);
		if(CollectionUtils.isNotEmpty(merchantList)){
			for(Merchant mer:merchantList){
				merchantCacheContexts.get(companyId).merchantMap.put(mer.getId(), mer);
			}
		}
	}

	private void reloadShopsByIds(List<Long> merchantIds, int companyId) throws Exception {
		List<Shop> shopList = merchantDao.getShopsByMerchantIds(merchantIds,companyId);
		if(CollectionUtils.isNotEmpty(shopList)){
			for(Shop shop:shopList){
				merchantCacheContexts.get(companyId).shopMap.put(shop.getMerchant_id(), shop);
			}
		}
	}

	private void reloadBusinessTimesByIds(List<Long> merchantIds, int companyId) throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int weekIndex = cal.get(Calendar.DAY_OF_WEEK) - 1;
		List<BusinessTime> allDay = new ArrayList<>();
		allDay.add(new BusinessTime("00:00:00","23:59:59"));
		List<BusinessTime> empty = new ArrayList<>();


		List<Long> merchantStoreCalendarIds = new ArrayList<>();
		List<MerchantStoreCalendar> merchantStoreCalendars = merchantDao.queryMerchantStoreCalendarsByIds(merchantIds,weekIndex,companyId);

		if(CollectionUtils.isEmpty(merchantStoreCalendars)){
			return;
		}
		for(MerchantStoreCalendar merchantStoreCalendar : merchantStoreCalendars){
			Integer type = merchantStoreCalendar.getType();
			if(MerchantStoreCalendarType.CUSTOM.getCode().equals(type)){
				merchantStoreCalendarIds.add(merchantStoreCalendar.getId());
			}
		}
		Map<Long,List<BusinessTime>> itemMap=new HashMap<Long,List<BusinessTime>>();

		if(CollectionUtils.isNotEmpty(merchantStoreCalendarIds)){
			List<MerchantStoreCalendarItem> merchantStoreCalendarItems = merchantDao.queryMerchantStoreCalendarItemsByIds(merchantStoreCalendarIds,companyId);
			if(CollectionUtils.isNotEmpty(merchantStoreCalendarItems)){
				for(MerchantStoreCalendarItem item : merchantStoreCalendarItems){
					Long merchantStoreCalendarId = item.getMerchantStoreCalendarId();
					BusinessTime businessTime = convert(item);
					List<BusinessTime> list = itemMap.get(merchantStoreCalendarId);
					if(CollectionUtils.isEmpty(list)){
						list = new ArrayList<>();
						itemMap.put(merchantStoreCalendarId,list);
					}
					list.add(businessTime);
				}
			}
		}


		if(CollectionUtils.isNotEmpty(merchantStoreCalendars)){
			for(MerchantStoreCalendar merchantStoreCalendar : merchantStoreCalendars){
				Integer type = merchantStoreCalendar.getType();
				Long merchantId = merchantStoreCalendar.getMerchantId();
				Long id = merchantStoreCalendar.getId();
				List<BusinessTime> list = null;
				if(MerchantStoreCalendarType.ALL_DAY.getCode().equals(type)){
					list = allDay;
				}else if(MerchantStoreCalendarType.RETIREE.getCode().equals(type)){
					list = empty;
				}else{
					list = itemMap.get(id);
				}
				merchantCacheContexts.get(companyId).businessTimesMap.put(merchantId,list);
			}
		}



	}
}
