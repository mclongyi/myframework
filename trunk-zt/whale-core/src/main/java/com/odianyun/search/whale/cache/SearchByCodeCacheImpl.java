package com.odianyun.search.whale.cache;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.odianyun.search.whale.api.model.req.SearchByCodeRequest;
import com.odianyun.search.whale.api.model.resp.SearchByCodeResponse;
import com.odianyun.search.whale.common.cache.CacheInfo;
import com.odianyun.search.whale.common.cache.ocache.MD5OCache;

public class SearchByCodeCacheImpl extends MD5OCache<SearchByCodeRequest, SearchByCodeResponse> {

	private static final long noCacheMaxTotalHit = 5l;

	public static SearchByCodeCacheImpl instance = new SearchByCodeCacheImpl();

	private SearchByCodeCacheImpl() {
	}

	/* (non-Javadoc)
	 * @see com.odianyun.search.whale.common.cache.ocache.BaseCache#filter(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected boolean filter(SearchByCodeRequest k, SearchByCodeResponse v) {
		Boolean isFilter = super.filter(k, v);
		if (isFilter) {
			return true;
		}
		if (v == null || v.getMerchantProducts() == null || v.getMerchantProducts().size() <= noCacheMaxTotalHit) {
			return true;
		}
		return false;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.odianyun.search.whale.common.cache.ocache.MD5SearchCache#get(java.
	 * lang.Object)
	 */
	@Override
	public SearchByCodeResponse get(SearchByCodeRequest k) {
		long start = System.currentTimeMillis();
		SearchByCodeResponse searchByCodeResponse = super.get(k);
		if (searchByCodeResponse != null) {
			CacheInfo cacheInfo = searchByCodeResponse.getCacheInfo();
			if (cacheInfo == null) {
				cacheInfo = new CacheInfo();
				cacheInfo.setCacheKey(generateKey(k));
			}
			cacheInfo.setCostTime(System.currentTimeMillis() - start);
			searchByCodeResponse.setCacheInfo(cacheInfo);
		}
		return searchByCodeResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.odianyun.search.whale.common.cache.ocache.MD5SearchCache#put(java.
	 * lang.Object, java.lang.Object)
	 */
	@Override
	public void put(SearchByCodeRequest k, SearchByCodeResponse v) {
		if (k != null && v != null) {
			CacheInfo cacheInfo = new CacheInfo();
			cacheInfo.setCacheKey(generateKey(k));
			cacheInfo.setCacheTime(new Date());
			v.setCacheInfo(cacheInfo);
			super.put(k, v);
			v.setCacheInfo(null);
		}
	}


}
