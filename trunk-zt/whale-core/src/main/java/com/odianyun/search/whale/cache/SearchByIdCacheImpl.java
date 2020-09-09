package com.odianyun.search.whale.cache;

import java.util.Date;
import java.util.List;

import com.odianyun.search.whale.api.model.req.SortType;
import org.apache.commons.collections.CollectionUtils;

import com.odianyun.search.whale.api.model.req.SearchByIdRequest;
import com.odianyun.search.whale.api.model.resp.SearchByIdResponse;
import com.odianyun.search.whale.common.cache.CacheInfo;
import com.odianyun.search.whale.common.cache.ocache.MD5OCache;

public class SearchByIdCacheImpl extends MD5OCache<SearchByIdRequest,SearchByIdResponse> {

	private static final long noCacheMaxTotalHit = 5l;

	public static SearchByIdCacheImpl instance = new SearchByIdCacheImpl();
	
	private SearchByIdCacheImpl(){}
	
	/* (non-Javadoc)
	 * @see com.odianyun.search.whale.common.cache.ocache.BaseCache#filter(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected boolean filter(SearchByIdRequest k, SearchByIdResponse v) {
		Boolean isFilter = super.filter(k, v);
		if (isFilter) {
			return true;
		}
		if (v == null || v.getMerchantProducts() == null || v.getMerchantProducts().size() <= noCacheMaxTotalHit) {
			return true;
		}
		return false;
	}
	/* (non-Javadoc)
	 * @see com.odianyun.search.whale.common.cache.ocache.MD5SearchCache#get(java.lang.Object)
	 */
	@Override
	public SearchByIdResponse get(SearchByIdRequest k) {
		long start = System.currentTimeMillis();
		SearchByIdResponse searchByIdResponse = super.get(k);
		if (searchByIdResponse != null) {
			CacheInfo cacheInfo = searchByIdResponse.getCacheInfo();
			if (cacheInfo == null) {
				cacheInfo = new CacheInfo();
				cacheInfo.setCacheKey(generateKey(k));
			}
			cacheInfo.setCostTime(System.currentTimeMillis() - start);
			searchByIdResponse.setCacheInfo(cacheInfo);
		}
		return searchByIdResponse;
	}

	/* (non-Javadoc)
	 * @see com.odianyun.search.whale.common.cache.ocache.MD5SearchCache#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void put(SearchByIdRequest k, SearchByIdResponse v) {
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
