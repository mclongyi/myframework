package com.odianyun.search.whale.data.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.odianyun.search.whale.data.model.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.data.dao.MerchantProductDao;
import com.odianyun.search.whale.data.service.MerchantProductService;

public class MerchantProductServiceImpl implements MerchantProductService{
	
	static final int BATCH_NUM = 2000;
	@Autowired
	MerchantProductDao productMerchantDao;

	@Override
	public Map<Long,MerchantProduct> getAllMerchantProducts(int companyId) throws Exception {
		List<MerchantProduct> merchantProducts= productMerchantDao.queryAllProductMerchant(companyId);
		Map<Long,MerchantProduct> ret = new HashMap<Long,MerchantProduct>();
		if(CollectionUtils.isNotEmpty(merchantProducts)){
			for(MerchantProduct mp:merchantProducts)
				ret.put(mp.getId(), mp);
		}
		return ret;
	}

	@Override
	public Map<Long,MerchantProduct> getMerchantProducts(
			List<Long> merchantProductIds, int companyId) throws Exception {
		List<MerchantProduct> merchantProducts = new ArrayList<>();
		List<Long> subMerchantProductIds = new ArrayList<>();
		
		for(Long merchantProductId : merchantProductIds){
			subMerchantProductIds.add(merchantProductId);
			if(subMerchantProductIds.size() == BATCH_NUM){
				merchantProducts.addAll(productMerchantDao.getMerchantProducts(subMerchantProductIds,companyId));
				subMerchantProductIds = new ArrayList<>();
			}
		}
		if(subMerchantProductIds.size() > 0){
			merchantProducts.addAll(productMerchantDao.getMerchantProducts(subMerchantProductIds,companyId));
		}
		
		Map<Long,MerchantProduct> ret = new HashMap<Long,MerchantProduct>();
		if(CollectionUtils.isNotEmpty(merchantProducts)){
			for(MerchantProduct mp:merchantProducts)
				ret.put(mp.getId(), mp);
		}
		return ret;
	}

	@Override
	public Map<Long, MerchantProductSimple> getMerchantProductsSimple(List<Long> merchantProductIds, int companyId) throws Exception {
		Map<Long, MerchantProductSimple> merchantProductSimpleMap=new HashMap<Long, MerchantProductSimple>();
		List<MerchantProductSimple> merchantProductSimples=productMerchantDao.queryMerchantProductsAllSimple(merchantProductIds,companyId);
		if(CollectionUtils.isNotEmpty(merchantProductSimples)){
			for(MerchantProductSimple merchantProductSimple:merchantProductSimples){
				merchantProductSimpleMap.put(merchantProductSimple.getId(),merchantProductSimple);
			}
		}
		return merchantProductSimpleMap;
	}

	@Override
	public List<MerchantProduct> getMerchantProductsWithPage(long maxId, int pageSize, int companyId) throws Exception {
		
		return 	productMerchantDao.getMerchantProductsWithPage(maxId,pageSize,companyId);
	}

	@Override
	public Map<Long, String> getMerchantProductUrls(List<Long> merchantProductIds, int companyId) throws Exception{
		
		return 	productMerchantDao.getMerchantProductUrls(merchantProductIds,companyId);
	}

	@Override
	public List<Integer> queryCompanyIds() throws Exception {
		return productMerchantDao.queryCompanyIds();
	}

	@Override
	public Map<Long, MerchantProduct> getMerchantProductsAll(List<Long> merchantProductIds, int companyId)
			throws Exception {
		List<MerchantProduct> merchantProducts = new ArrayList<>();
		List<Long> subMerchantProductIds = new ArrayList<>();
		
		for(Long merchantProductId : merchantProductIds){
			subMerchantProductIds.add(merchantProductId);
			if(subMerchantProductIds.size() == BATCH_NUM){
				merchantProducts.addAll(productMerchantDao.getMerchantProductsAll(subMerchantProductIds,companyId));
				subMerchantProductIds = new ArrayList<>();
			}
		}
		if(subMerchantProductIds.size() > 0){
			merchantProducts.addAll(productMerchantDao.getMerchantProductsAll(subMerchantProductIds,companyId));
		}
		
		Map<Long,MerchantProduct> ret = new HashMap<Long,MerchantProduct>();
		if(CollectionUtils.isNotEmpty(merchantProducts)){
			for(MerchantProduct mp:merchantProducts)
				ret.put(mp.getId(), mp);
		}
		return ret;
	}

	@Override
	public Map<Long, Long> getMerchantIdByMPId(List<Long> mpIds, int companyId) throws Exception {
		return productMerchantDao.queryMerchantIdByMPId(mpIds,companyId);
	}

	@Override
	public List<MerchantProductForSuggest> getMerchantProductsForSuggWithPage(long maxId, int pageSize, int companyId) throws Exception {
		return productMerchantDao.getMerchantProductsForSuggWithPage(maxId,pageSize,companyId);
	}

	@Override
	public List<MerchantProductForSuggest> getPointMerchantProductsForSuggWithPage(long maxId, int pageSize, Integer companyId) throws Exception {
		return productMerchantDao.getPointMerchantProductsForSuggWithPage(maxId, pageSize, companyId);
	}

	@Override
	public List<MerchantProduct> getMerchantProductList(List<Long> merchantProductIds, int companyId) throws Exception {
		List<MerchantProduct> merchantProducts = new ArrayList<>();
		List<Long> subMerchantProductIds = new ArrayList<>();

		for(Long merchantProductId : merchantProductIds){
			subMerchantProductIds.add(merchantProductId);
			if(subMerchantProductIds.size() == BATCH_NUM){
				merchantProducts.addAll(productMerchantDao.getMerchantProducts(subMerchantProductIds,companyId));
				subMerchantProductIds = new ArrayList<>();
			}
		}
		if(subMerchantProductIds.size() > 0){
			merchantProducts.addAll(productMerchantDao.getMerchantProducts(subMerchantProductIds,companyId));
		}

		return merchantProducts;
	}
	@Override
	public Map<Long,MerchantProductRelation> getStoreMerchantProductRelation(List<Long> mpIds, int companyId) throws Exception {
		Map<Long,MerchantProductRelation> retMap = new HashMap<Long,MerchantProductRelation>();
		List<MerchantProductRelation> list =  productMerchantDao.getStoreMerchantProductRelation(mpIds,companyId);
		if(CollectionUtils.isNotEmpty(list)){
			for(MerchantProductRelation pr : list){
				retMap.put(pr.getMpId(),pr);
			}
		}
		return retMap;
	}
	@Override
	public Map<Long, Long> queryPMPIdsBySMPIds(List<Long> mpIds, int companyId) throws Exception {
		Map<Long,Long> PMPIdsMap = new HashMap<Long,Long>();
		List<MerchantProductRelation> PMPlist = productMerchantDao.queryPMPIdsBySMPIds(mpIds,companyId);
		if(CollectionUtils.isNotEmpty(PMPlist)){
			for(MerchantProductRelation mp: PMPlist){
				PMPIdsMap.put(mp.getMpId(),mp.getRefId());
			}
		}
		return PMPIdsMap;
	}

	@Override
	public Map<Long, List<Long>> querySMPIdsByPMPIds(List<Long> mpIds, int companyId) throws Exception {
		Map<Long,List<Long>> SMPIdsMap = new HashMap<Long,List<Long>>();
		List<MerchantProductRelation> SMPlist = productMerchantDao.querySMPIdsByPMPIds(mpIds,companyId);
		if(CollectionUtils.isNotEmpty(SMPlist)){
			for(MerchantProductRelation mp:SMPlist){
				List<Long> smpIds = SMPIdsMap.get(mp.getRefId());
				if(smpIds==null){
					smpIds = new ArrayList<Long>();
					SMPIdsMap.put(mp.getRefId(),smpIds);
				}
				smpIds.add(mp.getMpId());
			}
		}
		return SMPIdsMap;
	}
}
