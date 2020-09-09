package com.odianyun.search.whale.api.model.resp;

import com.odianyun.search.whale.common.cache.CacheInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class ShopSearchResponse implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<ShopSearchResult> shopResult;

	private List<ShopSearchResult> zeroRecommendShopResult;

	public int companyId;
	
	//处理花费时间，毫秒
	public long costTime;

	private long totalHit;

	//缓存的key值
	private CacheInfo cacheInfo;

	public List<ShopSearchResult> getShopResult() {
		return shopResult;
	}

	public void setShopResult(List<ShopSearchResult> shopResult) {
		this.shopResult = shopResult;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public long getCostTime() {
		return costTime;
	}

	public void setCostTime(long costTime) {
		this.costTime = costTime;
	}

	public CacheInfo getCacheInfo() {
		return cacheInfo;
	}

	public void setCacheInfo(CacheInfo cacheInfo) {
		this.cacheInfo = cacheInfo;
	}

	public long getTotalHit() {
		return totalHit;
	}

	public void setTotalHit(long totalHit) {
		this.totalHit = totalHit;
	}

	public List<ShopSearchResult> getZeroRecommendShopResult() {
		return zeroRecommendShopResult;
	}

	public void setZeroRecommendShopResult(List<ShopSearchResult> zeroRecommendShopResult) {
		this.zeroRecommendShopResult = zeroRecommendShopResult;
	}
}
