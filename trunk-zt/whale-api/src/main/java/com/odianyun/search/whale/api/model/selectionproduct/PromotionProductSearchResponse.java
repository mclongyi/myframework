package com.odianyun.search.whale.api.model.selectionproduct;

import java.util.List;

import com.odianyun.search.whale.api.model.MerchantProduct;
import com.odianyun.search.whale.common.cache.CacheInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PromotionProductSearchResponse implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5048910856183903083L;
	//商品结果
	private List<MerchantProduct> merchantProducts;	
	//产品结果
//	private List<PromotionProduct> products;
	//搜索到的总商品数，非返回数
	private long totalHit;
	
	//缓存的key值
	private CacheInfo cacheInfo;
	
	public long getTotalHit() {
		return totalHit;
	}

	public void setTotalHit(long totalHit) {
		this.totalHit = totalHit;
	}

	public List<MerchantProduct> getMerchantProducts() {
		return merchantProducts;
	}

	public void setMerchantProducts(List<MerchantProduct> merchantProducts) {
		this.merchantProducts = merchantProducts;
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


}
