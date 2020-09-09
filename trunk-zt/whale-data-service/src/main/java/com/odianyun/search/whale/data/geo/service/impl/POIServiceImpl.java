package com.odianyun.search.whale.data.geo.service.impl;

import java.util.*;

import com.odianyun.search.whale.data.model.Merchant;
import com.odianyun.search.whale.data.model.geo.MerchantFlag;
import org.apache.commons.collections.CollectionUtils;

import com.odianyun.search.whale.data.geo.dao.POIDao;
import com.odianyun.search.whale.data.geo.service.POIService;
import com.odianyun.search.whale.data.model.geo.DeliveryArea;
import com.odianyun.search.whale.data.model.geo.POI;
import com.odianyun.search.whale.data.service.AbstractCompanyDBService;
import com.odianyun.search.whale.index.api.common.IndexConstants;

public class POIServiceImpl extends AbstractCompanyDBService implements POIService {

	
	POIDao poiDao;

	Map<Integer,POICacheContext> poiCacheContexts=new HashMap<Integer,POICacheContext>();


	@Override
	public List<POI> getShopPOIById(Long merchantId,int companyId) throws Exception {
		List<POI> list = new ArrayList<>();
		POICacheContext poiCacheContext = poiCacheContexts.get(companyId);
		if(null != poiCacheContext){
			list =  poiCacheContext.poiMap.get(merchantId);
		}
		return list;
//		return this.poiMap.get(merchantId);
	}

	@Override
	public Map<Long, List<POI>> getShopPOIsByIds(List<Long> merchantIds,int companyId)
			throws Exception {
		Map<Long, List<POI>> map = new HashMap<Long, List<POI>>();
		POICacheContext poiCacheContext = poiCacheContexts.get(companyId);
		if(null != poiCacheContext && CollectionUtils.isNotEmpty(merchantIds)){
			for(Long mId:merchantIds){
				List<POI> list = poiCacheContext.poiMap.get(mId);
				if(list!=null&& list.size()>0)
					map.put(mId, list);
			}
		}
		/*for(Long mId:merchantIds){
			List<POI> list = this.poiMap.get(mId);
			if(list!=null&& list.size()>0)
				map.put(mId, list);
		}*/
		return map;
	}

	@Override
	public List<DeliveryArea> getShopDeliveryAreaById(Long merchantId,int companyId)
			throws Exception {
		List<DeliveryArea> list = new ArrayList<>();
		POICacheContext poiCacheContext = poiCacheContexts.get(companyId);
		if(null != poiCacheContext){
			list = poiCacheContext.deliveryMap.get(merchantId);
		}
		return list;
//		return this.deliveryMap.get(merchantId);
	}

	@Override
	public Map<Long, List<DeliveryArea>> getShopDeliveryAreaMap(
			List<Long> merchantIds,int companyId) throws Exception {
		Map<Long, List<DeliveryArea>> ret = new HashMap<Long, List<DeliveryArea>>();
		POICacheContext poiCacheContext = poiCacheContexts.get(companyId);
		if(null != poiCacheContext && CollectionUtils.isNotEmpty(merchantIds)){
			for(Long mId:merchantIds){
				List<DeliveryArea> list = poiCacheContext.deliveryMap.get(mId);
				if(list!=null&& list.size()>0)
					ret.put(mId, list);
			}
		}
		
		/*for(Long mId:merchantIds){
			List<DeliveryArea> list = this.deliveryMap.get(mId);
			if(list!=null&& list.size()>0)
				ret.put(mId, list);
		}*/
		return ret;
	}

	@Override
	public List<MerchantFlag> getMerchantFlags(Long merchantId, int companyId) throws Exception {
		POICacheContext poiCacheContext = poiCacheContexts.get(companyId);
		List<MerchantFlag> ret = new ArrayList<MerchantFlag>();
		if(poiCacheContext != null){
			ret =  poiCacheContext.merchantFlagMap.get(merchantId);
		}
		return ret;
	}

	public void loadPOIData(POICacheContext poiCacheContext,int companyId) throws Exception{
		List<POI> poiList = poiDao.queryAllPOIs(companyId);
		if(poiList!=null && ! poiList.isEmpty()){
			Map<Long,List<POI>> tempMap =new HashMap<Long,List<POI>>();
			for(POI poi:poiList){
				List<POI> list = tempMap.get(poi.getRefId());
				if(list==null){
					list = new ArrayList<POI>();
				}
				list.add(poi);
				tempMap.put(poi.getRefId(), list);
			}
			poiCacheContext.poiMap = tempMap;
		}
	}
	
	public void loadDeliveryArea(POICacheContext poiCacheContext,int companyId) throws Exception{
		int pageNo = 1;
		boolean hasNext = true;
		List<DeliveryArea> deliverylist;
		Map<Long,List<DeliveryArea>> tempMap = new HashMap<Long,List<DeliveryArea>>();
		while(hasNext){
			deliverylist=poiDao.queryDeliveryAreasWithPage(pageNo, IndexConstants.pageSize,companyId);
			if(deliverylist==null || deliverylist.isEmpty()|| 
					deliverylist.size() < IndexConstants.pageSize){
				hasNext = false;
			}
			if(deliverylist!=null){
				for(DeliveryArea da:deliverylist){
					List<DeliveryArea> arealist = tempMap.get(da.getMerchant_id());
					if(arealist==null){
						arealist =new ArrayList<DeliveryArea>();
					}
					arealist.add(da);
					tempMap.put(da.getMerchant_id(), arealist);
				}
			}
			pageNo ++;
		}
		if(tempMap.size()>0){
			poiCacheContext.deliveryMap = tempMap;
		}
	}

	private void loadMerchantFlag(POICacheContext poiCacheContext, int companyId) throws Exception {
		List<MerchantFlag> flagList = poiDao.queryAllFlags(companyId);
		if(flagList!=null && CollectionUtils.isNotEmpty(flagList)){
			Map<Long,List<MerchantFlag>> tempMap = new HashMap<Long,List<MerchantFlag>>();
			for(MerchantFlag flag : flagList){
				List<MerchantFlag> flags = tempMap.get(flag.getMerchantId());
				if(flags==null){
					flags = new ArrayList<MerchantFlag>();
				}
				flags.add(flag);
				tempMap.put(flag.getMerchantId(),flags);
			}
			poiCacheContext.merchantFlagMap = tempMap;

		}


	}
	
	@Override
	protected void tryReload(int companyId) throws Exception {
		POICacheContext poiCacheContext = new POICacheContext();
		loadDeliveryArea(poiCacheContext,companyId);
		loadPOIData(poiCacheContext,companyId);
		loadMerchantFlag(poiCacheContext,companyId);
		poiCacheContexts.put(companyId,poiCacheContext);
		
	}
	@Override
	public void tryReload(List<Long> merchantIds,int companyId) throws Exception{
		if(!poiCacheContexts.containsKey(companyId)){
			poiCacheContexts.put(companyId,new POICacheContext());
		}
		reLoadPoiByIds(merchantIds,companyId);
		reLoadDeliveryAreByIds(merchantIds,companyId);
		reLoadFlagByIds(merchantIds,companyId);
	}

	private void reLoadPoiByIds(List<Long> merchantIds, int companyId) throws Exception{
		List<POI> pois = poiDao.getPOIsByIds(merchantIds,companyId);
		if(pois!=null && CollectionUtils.isNotEmpty(pois)){
			Map<Long,List<POI>> tmpMap = poiCacheContexts.get(companyId).poiMap;
			if(tmpMap==null){
				tmpMap = new HashMap<Long,List<POI>>();
			}
			for(Long id:merchantIds){
				if(tmpMap.containsKey(id)){
					tmpMap.remove(id);
				}
			}
			for(POI poi : pois) {
				List<POI> list = tmpMap.get(poi.getRefId());
				if (list == null) {
					list = new ArrayList<POI>();
				}
				list.add(poi);
				tmpMap.put(poi.getRefId(), list);
			}
			poiCacheContexts.get(companyId).poiMap = tmpMap;
		}
	}
	private void reLoadDeliveryAreByIds(List<Long> merchantIds, int companyId) throws Exception{
		List<DeliveryArea> lists = poiDao.queryDeliveryAreasByIds(merchantIds,companyId);
		if(lists!=null && CollectionUtils.isNotEmpty(lists)){
			Map<Long,List<DeliveryArea>> tmpMap = poiCacheContexts.get(companyId).deliveryMap;
			if(tmpMap == null){
				tmpMap = new HashMap<Long,List<DeliveryArea>>();
			}
			for(Long id:merchantIds){
				if(tmpMap.containsKey(id)){
					tmpMap.remove(id);
				}
			}
			for(DeliveryArea da : lists){
				List<DeliveryArea> tmpList = tmpMap.get(da.getMerchant_id());
				if(tmpList == null){
					tmpList = new ArrayList<DeliveryArea>();
				}
				tmpList.add(da);
				tmpMap.put(da.getMerchant_id(),tmpList);
			}
			poiCacheContexts.get(companyId).deliveryMap = tmpMap;
		}

	}
	private void reLoadFlagByIds(List<Long> merchantIds, int companyId) throws Exception{
		List<MerchantFlag> flags = poiDao.queryFlagsByIds(merchantIds,companyId);
		if(flags != null && CollectionUtils.isNotEmpty(flags)){
			Map<Long,List<MerchantFlag>> flagMap = poiCacheContexts.get(companyId).merchantFlagMap;
			if(flagMap==null){
				flagMap = new HashMap<Long,List<MerchantFlag>>();
			}
			for(Long id:merchantIds){
				if(flagMap.containsKey(id)){
					flagMap.remove(id);
				}
			}
			for(MerchantFlag flag : flags){
				List<MerchantFlag> flagList = flagMap.get(flag.getMerchantId());
				if(null == flagList){
					flagList = new ArrayList<MerchantFlag>();
				}
				flagList.add(flag);
				flagMap.put(flag.getMerchantId(),flagList);
			}
			poiCacheContexts.get(companyId).merchantFlagMap = flagMap;
 		}
	}


	@Override
	public int getInterval() {
		// TODO Auto-generated method stub
		return 30;
	}

	public POIDao getPoiDao() {
		return poiDao;
	}

	public void setPoiDao(POIDao poiDao) {
		this.poiDao = poiDao;
	}
	
	private static class POICacheContext{
		
		Map<Long,List<POI>> poiMap =new HashMap<Long,List<POI>>();
		Map<Long,List<DeliveryArea>> deliveryMap = new HashMap<Long,List<DeliveryArea>>();
		Map<Long,List<MerchantFlag>> merchantFlagMap = new HashMap<Long,List<MerchantFlag>>();
	}
	

}
