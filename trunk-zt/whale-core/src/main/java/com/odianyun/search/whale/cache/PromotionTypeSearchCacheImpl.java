package com.odianyun.search.whale.cache;

import com.odianyun.search.whale.api.model.SearchResponse;
import com.odianyun.search.whale.api.model.constants.SearchType;
import com.odianyun.search.whale.api.model.req.PromotionTypeSearchRequest;
import com.odianyun.search.whale.api.model.req.SearchRequest;
import com.odianyun.search.whale.api.model.req.SortType;
import com.odianyun.search.whale.api.model.resp.PromotionSearchResponse;
import com.odianyun.search.whale.api.model.resp.PromotionTypeSearchResponse;
import com.odianyun.search.whale.common.cache.CacheInfo;
import com.odianyun.search.whale.common.cache.ICache;
import com.odianyun.search.whale.common.cache.ocache.BaseCache;
import com.odianyun.search.whale.common.cache.ocache.MD5OCache;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * Created by zengfenghua on 16/10/22.
 */
public class PromotionTypeSearchCacheImpl extends MD5OCache<PromotionTypeSearchRequest, PromotionTypeSearchResponse> {

	public static ICache<PromotionTypeSearchRequest, PromotionTypeSearchResponse> instance = new PromotionTypeSearchCacheImpl();

	private static final int cacheTime = 5;

	private PromotionTypeSearchCacheImpl() {
	};

	/**
	 * 把Key放入searchResponse
	 * 
	 * @param searchRequest
	 * @return
	 */
	@Override
	public PromotionTypeSearchResponse get(PromotionTypeSearchRequest searchRequest) {
		long start = System.currentTimeMillis();
		PromotionTypeSearchResponse response = super.get(searchRequest);
		if (response != null) {
			CacheInfo cacheInfo = response.getCacheInfo();
			if (cacheInfo == null) {
				cacheInfo = new CacheInfo();
				cacheInfo.setCacheKey(generateKey(searchRequest));
			}
			cacheInfo.setCostTime(System.currentTimeMillis() - start);
			response.setCacheInfo(cacheInfo);
		}
		return response;

	}

	@Override
	public void put(PromotionTypeSearchRequest searchRequest, PromotionTypeSearchResponse searchResponse) {
		if (searchRequest != null && searchResponse != null) {
			CacheInfo cacheInfo = new CacheInfo();
			cacheInfo.setCacheKey(generateKey(searchRequest));
			cacheInfo.setCacheTime(new Date());
			searchResponse.setCacheInfo(cacheInfo);
			super.put(searchRequest, searchResponse);
			searchResponse.setCacheInfo(null);
		}

	}

	@Override
	protected int caclCacheTime(PromotionTypeSearchRequest promotionTypeSearchRequest, PromotionTypeSearchResponse promotionTypeSearchResponse) {
		return cacheTime;
	}
}
