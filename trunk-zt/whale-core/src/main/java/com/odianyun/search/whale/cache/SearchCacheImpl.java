package com.odianyun.search.whale.cache;

import com.google.gson.Gson;
import com.odianyun.search.whale.api.model.SearchResponse;
import com.odianyun.search.whale.api.model.req.SearchRequest;
import com.odianyun.search.whale.api.model.req.SortType;
import com.odianyun.search.whale.common.cache.CacheInfo;
import com.odianyun.search.whale.common.cache.ICache;
import com.odianyun.search.whale.common.cache.ocache.BaseCache;
import com.odianyun.search.whale.common.cache.ocache.MD5OCache;
import com.odianyun.search.whale.common.util.GsonUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * Created by zengfenghua on 16/10/22.
 */
public class SearchCacheImpl extends MD5OCache<SearchRequest, SearchResponse> {

	private static final long noCacheMaxTotalHit = 20l;

	private static final long cacheMinTotalHit = 200l;

	private static final long cacheMediumTotalHit = 1000l;

	private static final long cacheMaxTotalHit = 2000l;

	private static final int miminCacheTime = 5;

	private static final int minCacheTime = 10;

	private static final int mediumCacheTime = 20;

	private static final int maxCacheTime = 60;

	public static ICache<SearchRequest, SearchResponse> instance = new SearchCacheImpl();

	private SearchCacheImpl() {
	};

	/**
	 * 是否过滤这个key,不满足缓存的条件
	 *
	 * @param searchRequest
	 * @return
	 */
	@Override
	protected boolean filterKey(SearchRequest searchRequest) {
		if (searchRequest == null) {
			return true;
		}
		if (CollectionUtils.isNotEmpty(searchRequest.getAttrValueIds())) {
			return true;
		}
		if (searchRequest.getAttrItemValuesMap() != null && searchRequest.getAttrItemValuesMap().size() > 0) {
			return true;
		}
		if (CollectionUtils.isNotEmpty(searchRequest.getCoverProvinceIds())) {
			return true;
		}
		if (CollectionUtils.isNotEmpty(searchRequest.getFilterTypes())) {
			return true;
		}
		if (searchRequest.getPriceRange() != null) {
			return true;
		}
		if (CollectionUtils.isNotEmpty(searchRequest.getExcludeMpIds())) {
			return true;
		}
		return false;
	}

	/**
	 * 是否过滤这个key和value,不满足缓存的条件
	 *
	 * @param searchRequest
	 * @return
	 */
	@Override
	protected boolean filter(SearchRequest searchRequest, SearchResponse searchResponse) {
		Boolean isFilter = super.filter(searchRequest, searchResponse);
		if (isFilter) {
			return true;
		}
		if (searchResponse == null || searchResponse.getTotalHit() <= noCacheMaxTotalHit) {
			return true;
		}
		return false;
	}


	/**
	 * 计算缓存的时间,单位为分钟
	 *
	 * @param searchRequest
	 * @param searchResponse
	 * @return
	 */
	@Override
	protected int caclCacheTime(SearchRequest searchRequest, SearchResponse searchResponse) {
		if (searchResponse.getTotalHit() > cacheMaxTotalHit) {
			return maxCacheTime;
		}
		if (searchResponse.getTotalHit() > cacheMediumTotalHit) {
			return mediumCacheTime;
		}
		if (searchResponse.getTotalHit() > cacheMinTotalHit) {
			return minCacheTime;
		}
		return miminCacheTime;
	}

	/**
	 * 把Key放入searchResponse忠
	 * 
	 * @param searchRequest
	 * @return
	 */
	@Override
	public SearchResponse get(SearchRequest searchRequest) {
		long start = System.currentTimeMillis();
		SearchResponse searchResponse = super.get(searchRequest);
		if (searchResponse != null) {
			CacheInfo cacheInfo = searchResponse.getCacheInfo();
			if (cacheInfo == null) {
				cacheInfo = new CacheInfo();
				cacheInfo.setCacheKey(generateKey(searchRequest));
			}
			cacheInfo.setCostTime(System.currentTimeMillis() - start);
			searchResponse.setCacheInfo(cacheInfo);
		}
		return searchResponse;

	}

	@Override
	public void put(SearchRequest searchRequest, SearchResponse searchResponse) {
		if (searchRequest != null && searchResponse != null) {
			CacheInfo cacheInfo = new CacheInfo();
			cacheInfo.setCacheKey(generateKey(searchRequest));
			cacheInfo.setCacheTime(new Date());
			searchResponse.setCacheInfo(cacheInfo);
			super.put(searchRequest, searchResponse);
			searchResponse.setCacheInfo(null);
		}

	}
}
