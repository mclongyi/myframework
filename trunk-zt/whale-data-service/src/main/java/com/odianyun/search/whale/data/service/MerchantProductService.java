package com.odianyun.search.whale.data.service;

import java.util.List;
import java.util.Map;

import com.odianyun.search.whale.data.model.*;

public interface MerchantProductService {
	
	public Map<Long,MerchantProduct> getAllMerchantProducts(int companyId) throws Exception;
	
	public Map<Long,MerchantProduct> getMerchantProducts(List<Long> merchantProductIds, int companyId) throws Exception;

	public Map<Long,MerchantProductSimple> getMerchantProductsSimple(List<Long> merchantProductIds, int companyId) throws Exception;
	
	public List<MerchantProduct> getMerchantProductList(List<Long> merchantProductIds, int companyId) throws Exception;

	public List<MerchantProduct> getMerchantProductsWithPage(long maxId, int pageSize, int companyId) throws Exception;

	public Map<Long, String> getMerchantProductUrls(List<Long> merchantProductIds, int companyId) throws Exception;

	public List<Integer> queryCompanyIds() throws Exception;

	public Map<Long, MerchantProduct> getMerchantProductsAll(List<Long> merchantProductIds, int companyId) throws Exception;

	public Map<Long,Long> getMerchantIdByMPId(List<Long> mpIds,int companyId) throws Exception;


	List<MerchantProductForSuggest> getMerchantProductsForSuggWithPage(long maxId, int pageSize, int companyId) throws Exception;

	List<MerchantProductForSuggest> getPointMerchantProductsForSuggWithPage(long maxId, int pageSize, Integer companyId) throws Exception;

	public Map<Long,MerchantProductRelation> getStoreMerchantProductRelation(List<Long> mpIds, int companyId) throws Exception;

	//根据门店商品id查询父商家商品id  只可能一对一 <门店商品id,父商家商品id>
	public Map<Long,Long> queryPMPIdsBySMPIds(List<Long> mpIds,int companyId) throws Exception;
	//根据父商家商品id查询子门店商品id  可能一对多 <父商家商品id,子门店商品ids>
	public Map<Long,List<Long>> querySMPIdsByPMPIds(List<Long> mpIds,int companyId) throws Exception;

}
