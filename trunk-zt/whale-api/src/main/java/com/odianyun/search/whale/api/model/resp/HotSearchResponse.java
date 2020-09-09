package com.odianyun.search.whale.api.model.resp;

import com.odianyun.search.whale.api.model.MerchantProduct;
import com.odianyun.search.whale.common.cache.CacheInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class HotSearchResponse implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//商品id的结果集
	public List<Long> merchantProductIds = new ArrayList<Long>();

	//merchantProduct结果集
	public List<MerchantProduct> merchantProductResult = new ArrayList<MerchantProduct>();

	public int companyId;

	private long totalHit;
	
	//处理花费时间，毫秒
	public long costTime;

	//缓存的key值
	private CacheInfo cacheInfo;

	public List<Long> getMerchantProductIds() {
		return merchantProductIds;
	}

	public void setMerchantProductIds(List<Long> merchantProductIds) {
		this.merchantProductIds = merchantProductIds;
	}

	public List<MerchantProduct> getMerchantProductResult() {
		return merchantProductResult;
	}

	public void setMerchantProductResult(List<MerchantProduct> merchantProductResult) {
		this.merchantProductResult = merchantProductResult;
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
}
