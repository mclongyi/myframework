package com.odianyun.search.whale.cache;

import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.odianyun.search.whale.api.model.selectionproduct.TypeOfProduct;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.common.lang3.StringUtils;

import com.odianyun.search.whale.api.model.req.SortType;
import com.odianyun.search.whale.api.model.selectionproduct.PromotionProductSearchRequest;
import com.odianyun.search.whale.api.model.selectionproduct.PromotionProductSearchResponse;
import com.odianyun.search.whale.common.cache.CacheInfo;
import com.odianyun.search.whale.common.cache.ocache.MD5OCache;

public class PromotionSelectionCacheImpl
		extends MD5OCache<PromotionProductSearchRequest, PromotionProductSearchResponse> {

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

	public static PromotionSelectionCacheImpl instance = new PromotionSelectionCacheImpl();

	private PromotionSelectionCacheImpl() {
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.odianyun.search.whale.common.cache.ocache.BaseSearchCache#get(java.
	 * lang.Object)
	 */
	@Override
	public PromotionProductSearchResponse get(PromotionProductSearchRequest k) {
		long start = System.currentTimeMillis();
		PromotionProductSearchResponse promotionProductSearchResponse = super.get(k);
		if (promotionProductSearchResponse != null) {
			CacheInfo cacheInfo = promotionProductSearchResponse.getCacheInfo();
			if (cacheInfo == null) {
				cacheInfo = new CacheInfo();
				cacheInfo.setCacheKey(generateKey(k));
			}
			cacheInfo.setCostTime(System.currentTimeMillis() - start);
			promotionProductSearchResponse.setCacheInfo(cacheInfo);
		}
		return promotionProductSearchResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.odianyun.search.whale.common.cache.ocache.BaseSearchCache#put(java.
	 * lang.Object, java.lang.Object)
	 */
	@Override
	public void put(PromotionProductSearchRequest k, PromotionProductSearchResponse v) {
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
	 * com.odianyun.search.whale.common.cache.ocache.BaseSearchCache#filter(java
	 * .lang.Object, java.lang.Object)
	 */
	@Override
	protected boolean filter(PromotionProductSearchRequest k, PromotionProductSearchResponse v) {
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
	 * @see com.odianyun.search.whale.common.cache.ocache.BaseSearchCache#
	 * caclCacheTime(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected int caclCacheTime(PromotionProductSearchRequest k, PromotionProductSearchResponse v) {
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
