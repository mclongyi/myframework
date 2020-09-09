package com.odianyun.search.whale.data.service;

import java.util.List;
import java.util.Map;

import com.odianyun.search.whale.data.model.Merchant;
import com.odianyun.search.whale.data.model.MerchantBelongArea;
import com.odianyun.search.whale.data.model.Shop;
import com.odianyun.search.whale.data.model.geo.BusinessTime;

public interface MerchantService {
	
	public List<Merchant> getMerchantsWithPage(int pageNo,int pageSize,int companyId) throws Exception;
	
	public Merchant getMerchantById(Long merchantId,int companyId) throws Exception;
	
	public Map<Long,Merchant> getMerchantsByIds(List<Long> merchantIds,int companyId) throws Exception;
	
	public Map<Long,String> getMerchantMapByIds(List<Long> merchantIds,int companyId)throws Exception;
	
	public String getMerchantName(Long merchantId,int companyId) throws Exception;

	public Shop getShopByShopName(String shopName, int companyId);

	public Map<Long,Shop> getShopMapByMerchantIds(List<Long> merchantIds,int companyId)throws Exception;
	
	public Shop getShop(Long merchantId,int companyId) throws Exception;
	
	public Map<Long,MerchantBelongArea> getBelongAreaByMerchantIds(List<Long> merchantIds,int companyId)throws Exception;
	
	public MerchantBelongArea getBelongAreaByMerchantId(Long merchantId,int companyId) throws Exception;

	List<BusinessTime> getMerchantBusinessTimes(Long merchantId, int companyId)throws Exception;
}
