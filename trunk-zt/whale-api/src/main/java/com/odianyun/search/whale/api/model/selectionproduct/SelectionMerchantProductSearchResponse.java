package com.odianyun.search.whale.api.model.selectionproduct;

import java.util.LinkedList;
import java.util.List;

import com.odianyun.search.whale.api.model.MerchantProduct;
import com.odianyun.search.whale.common.cache.CacheInfo;
import lombok.Data;

@Data
public class SelectionMerchantProductSearchResponse implements java.io.Serializable{


	public SelectionMerchantProductSearchResponse() {
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6234843473776900313L;
	
	//对应的商品
	private List<MerchantProduct> merchantProducts=new LinkedList<MerchantProduct>();
	//搜索到的总商品数，非返回数
	private long totalHit;
	
	//缓存的key值
	private CacheInfo cacheInfo;
	
	public List<MerchantProduct> getMerchantProducts() {
		return merchantProducts;
	}

	public void setMerchantProducts(List<MerchantProduct> merchantProducts) {
		this.merchantProducts = merchantProducts;
	}
	/**
	 * @return the totalHit
	 */
	public long getTotalHit() {
		return totalHit;
	}

	/**
	 * @param totalHit the totalHit to set
	 */
	public void setTotalHit(long totalHit) {
		this.totalHit = totalHit;
	}
	
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
	
	@Override
	public String toString() {
		return "SelectionMerchantProductSearchResponse [merchantProducts=" + merchantProducts + "]";
	}

}
