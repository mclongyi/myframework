package com.odianyun.search.whale.api.model.resp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.odianyun.search.whale.api.model.MerchantProduct;
import com.odianyun.search.whale.common.cache.CacheInfo;
import lombok.Data;

@Data
public class SearchByIdResponse implements Serializable{
	public SearchByIdResponse() {
	}

	Map<Long,MerchantProduct> merchantProducts = new HashMap<Long,MerchantProduct>();

	List<MerchantProduct> merchantProductList = new ArrayList<>();

	//缓存的key值
	private CacheInfo cacheInfo;

	private long totalHit;

	/**
	 * @return the cacheInfo
	 */
	public CacheInfo getCacheInfo() {
		return cacheInfo;
	}

	/**
	 * @param cacheInfo the cacheInfo to set
	 */
	public void setCacheInfo(CacheInfo cacheInfo) {
		this.cacheInfo = cacheInfo;
	}

	public Map<Long, MerchantProduct> getMerchantProducts() {
		return merchantProducts;
	}

	public void setMerchantProducts(Map<Long, MerchantProduct> merchantProducts) {
		this.merchantProducts = merchantProducts;
	}

	public List<MerchantProduct> getMerchantProductList() {
		return merchantProductList;
	}

	public void setMerchantProductList(List<MerchantProduct> merchantProductList) {
		this.merchantProductList = merchantProductList;
	}

	public long getTotalHit() {
		return totalHit;
	}

	public void setTotalHit(long totalHit) {
		this.totalHit = totalHit;
	}
}
