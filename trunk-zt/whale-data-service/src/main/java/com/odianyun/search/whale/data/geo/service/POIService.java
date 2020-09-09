package com.odianyun.search.whale.data.geo.service;

import java.util.List;
import java.util.Map;

import com.odianyun.search.whale.data.model.geo.DeliveryArea;
import com.odianyun.search.whale.data.model.geo.MerchantFlag;
import com.odianyun.search.whale.data.model.geo.POI;

public interface POIService {

	public List<POI> getShopPOIById(Long merchantId,int companyId) throws Exception;
	
	public Map<Long,List<POI>> getShopPOIsByIds(List<Long> merchantIds,int companyId) throws Exception;
	
	public List<DeliveryArea> getShopDeliveryAreaById(Long merchantId,int companyId) throws Exception;
	
	public Map<Long,List<DeliveryArea>> getShopDeliveryAreaMap(List<Long> merchantIds,int companyId) throws Exception;

	public List<MerchantFlag> getMerchantFlags(Long merchantId, int companyId) throws Exception;

	//public void reLoadByMerchantIds(List<Long> merchantIds,int companyIds) throws Exception;

}
