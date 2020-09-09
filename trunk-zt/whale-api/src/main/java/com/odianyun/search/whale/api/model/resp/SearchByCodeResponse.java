package com.odianyun.search.whale.api.model.resp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.odianyun.search.whale.api.model.MerchantProduct;
import com.odianyun.search.whale.common.cache.CacheInfo;
import lombok.Data;

@Data
public class SearchByCodeResponse implements Serializable{

	private static final long serialVersionUID = -2575828692563059292L;

	public SearchByCodeResponse() {
	}

	Map<String,MerchantProduct> merchantProducts = new HashMap<String,MerchantProduct>();
	
	//缓存的key值
	private CacheInfo cacheInfo;
	
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
	
	/**
	 * @return the merchantProducts
	 */
	public Map<String, MerchantProduct> getMerchantProducts() {
		return merchantProducts;
	}

	/**
	 * @param merchantProducts the merchantProducts to set
	 */
	public void setMerchantProducts(Map<String, MerchantProduct> merchantProducts) {
		this.merchantProducts = merchantProducts;
	}
	
}
