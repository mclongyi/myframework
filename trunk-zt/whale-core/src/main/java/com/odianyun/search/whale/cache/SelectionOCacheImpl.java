package com.odianyun.search.whale.cache;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.odianyun.search.whale.api.model.PriceRange;
import com.odianyun.search.whale.api.model.req.SortType;
import com.odianyun.search.whale.api.model.selectionproduct.SelectionMerchantProductSearchResponse;
import com.odianyun.search.whale.api.model.selectionproduct.SelectionProductSearchRequest;
import com.odianyun.search.whale.common.cache.CacheInfo;
import com.odianyun.search.whale.common.cache.ocache.MD5OCache;

public class SelectionOCacheImpl
		extends MD5OCache<SelectionProductSearchRequest, SelectionMerchantProductSearchResponse> {

	private static final long noCacheMaxTotalHit = 20l;

	private static final long cacheMinTotalHit = 200l;

	private static final long cacheMediumTotalHit = 1000l;

	private static final long cacheMaxTotalHit = 2000l;
	
	private static final long cacheMinCount = 200l;

	private static final long cacheMediumCount = 1000l;

	private static final long cacheMaxCount = 2000l;

	private static final int miminCacheTime = 5;

	private static final int minCacheTime = 10;

	private static final int mediumCacheTime = 20;

	private static final int maxCacheTime = 60;

	public static SelectionOCacheImpl instance = new SelectionOCacheImpl();

	private SelectionOCacheImpl() {
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.odianyun.search.whale.common.cache.ocache.MD5SearchCache#get(java.
	 * lang.Object)
	 */
	@Override
	public SelectionMerchantProductSearchResponse get(SelectionProductSearchRequest k) {
		long start = System.currentTimeMillis();
		SelectionMerchantProductSearchResponse selectionMerchantProductSearchResponse = super.get(k);
		if (selectionMerchantProductSearchResponse != null) {
			CacheInfo cacheInfo = selectionMerchantProductSearchResponse.getCacheInfo();
			if (cacheInfo == null) {
				cacheInfo = new CacheInfo();
				cacheInfo.setCacheKey(generateKey(k));
			}
			cacheInfo.setCostTime(System.currentTimeMillis() - start);
			selectionMerchantProductSearchResponse.setCacheInfo(cacheInfo);
		}
		return selectionMerchantProductSearchResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.odianyun.search.whale.common.cache.ocache.MD5SearchCache#put(java.
	 * lang.Object, java.lang.Object)
	 */
	@Override
	public void put(SelectionProductSearchRequest k, SelectionMerchantProductSearchResponse v) {
		if (k != null && v != null) {
			CacheInfo cacheInfo = new CacheInfo();
			cacheInfo.setCacheKey(generateKey(k));
			cacheInfo.setCacheTime(new Date());
			v.setCacheInfo(cacheInfo);
			super.put(k, v);
			v.setCacheInfo(null);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.odianyun.search.whale.common.cache.ocache.MD5SearchCache#filter(java.
	 * lang.Object, java.lang.Object)
	 */
	@Override
	protected boolean filter(SelectionProductSearchRequest k, SelectionMerchantProductSearchResponse v) {
		Boolean isFilter = super.filter(k, v);
		if (isFilter) {
			return true;
		}
		if (v == null || v.getTotalHit() <= noCacheMaxTotalHit) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.odianyun.search.whale.common.cache.ocache.MD5SearchCache#
	 * caclCacheTime(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected int caclCacheTime(SelectionProductSearchRequest k, SelectionMerchantProductSearchResponse v) {
		if (v.getTotalHit() > cacheMaxTotalHit || k.getCount() > cacheMaxCount) {
			return maxCacheTime;
		}
		if (v.getTotalHit() > cacheMediumTotalHit || k.getCount() > cacheMediumCount) {
			return mediumCacheTime;
		}
		if (v.getTotalHit() > cacheMinTotalHit || k.getCount() > cacheMinCount) {
			return minCacheTime;
		}
		return miminCacheTime;
	}
}
