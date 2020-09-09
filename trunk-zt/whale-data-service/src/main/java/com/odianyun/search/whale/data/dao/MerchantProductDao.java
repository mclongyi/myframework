package com.odianyun.search.whale.data.dao;

import java.util.List;
import java.util.Map;

import com.odianyun.search.whale.data.model.*;

public interface MerchantProductDao {
	
	/**
	 * 读取所有的商品数据,适用于全量索引
	 * @return
	 * @throws Exception
	 */
	public List<MerchantProduct> queryAllProductMerchant(int companyId) throws Exception; 
	
	/**
	 * 根据商品ID列表读取相关的商品信息，适用于实时索引
	 * @param merchantProductIds
	 * @return
	 * @throws Exception
	 */
	public List<MerchantProduct> getMerchantProducts(List<Long> merchantProductIds, int companyId) throws Exception;
	
	/**
	 * 根据时间跨度查询相应的商品信息，适用于实时索引
	 * @param updateTimeRange
	 * @return
	 * @throws Exception
	 */
	public List<Long> queryProductMerchantIdsByUpdateTime(UpdateTimeRange updateTimeRange, int companyId) throws Exception;
	
	/**
	 * 分页获取MerchantProducts数据，适用于全量索引
	 * @return
	 * @throws Exception
	 */
	public List<MerchantProduct> getMerchantProductsWithPage(long maxId, int pageSize, int companyId) throws Exception;

	public Map<Long, String> getMerchantProductUrls(List<Long> merchantProductIds, int companyId) throws Exception;

	public List<Long> queryProductMerchantIdsByProductIds(List<Long> updateProductIds, int companyId) throws Exception;
 
	public List<Integer> queryCompanyIds() throws Exception;

	public List<MerchantProduct> getMerchantProductsAll(List<Long> merchantProductIds,
			int companyId) throws Exception;

	public List<Long> queryProductIdsByMpIds(List<Long> ids, int companyId) throws Exception;

	public List<MerchantProductSimple> queryMerchantProductsAllSimple(List<Long> merchantProductIds, int companyId) throws Exception;

	//查询所有关联ean码
	public List<String> queryAllBindEans(Long merchantProductId, int companyId) throws Exception;

	public Map<Long,Long> queryMerchantIdByMPId(List<Long> mpIds,int companyId) throws Exception;

	List<MerchantProductForSuggest> getMerchantProductsForSuggWithPage(long maxId, int pageSize, int companyId) throws Exception;

	List<MerchantProductForSuggest> getPointMerchantProductsForSuggWithPage(long maxId, int pageSize, Integer companyId) throws Exception;

	//根据商品id查询关联的父商家商品id
	List<MerchantProductRelation> getStoreMerchantProductRelation(List<Long> mpIds,int companyId) throws Exception;
	//根据门店商品id查询父商家商品id
	List<MerchantProductRelation> queryPMPIdsBySMPIds(List<Long> mpIds,int companyId) throws Exception;
	//根据父商家商品id查询子门店商品id
	List<MerchantProductRelation> querySMPIdsByPMPIds(List<Long> mpIds,int companyId) throws Exception;
	//根据商品id查询所有有关联的父子商品
	List<MerchantProductRelation> queryRelationByIds(List<Long> mpIds,int companyId) throws Exception;

}
