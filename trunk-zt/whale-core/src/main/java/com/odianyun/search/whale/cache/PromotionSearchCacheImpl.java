package com.odianyun.search.whale.cache;

import com.odianyun.search.whale.api.model.constants.SearchType;
import com.odianyun.search.whale.api.model.req.PromotionSearchRequest;
import com.odianyun.search.whale.api.model.req.PromotionTypeSearchRequest;
import com.odianyun.search.whale.api.model.req.SortType;
import com.odianyun.search.whale.api.model.resp.PromotionSearchResponse;
import com.odianyun.search.whale.common.cache.CacheInfo;
import com.odianyun.search.whale.common.cache.ICache;
import com.odianyun.search.whale.common.cache.ocache.MD5OCache;
import org.apache.commons.collections.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by zengfenghua on 16/10/22.
 */
public class PromotionSearchCacheImpl extends MD5OCache<PromotionSearchRequest, PromotionSearchResponse> {

	public static ICache<PromotionSearchRequest, PromotionSearchResponse> instance = new PromotionSearchCacheImpl();

	private static final int cacheTime = 5;

	private PromotionSearchCacheImpl() {
	};


	/**
	 * 把Key放入searchResponse
	 * 
	 * @param searchRequest
	 * @return
	 */
	@Override
	public PromotionSearchResponse get(PromotionSearchRequest searchRequest) {
		long start = System.currentTimeMillis();
		PromotionSearchResponse response = super.get(searchRequest);
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
	public void put(PromotionSearchRequest searchRequest, PromotionSearchResponse searchResponse) {
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
	protected int caclCacheTime(PromotionSearchRequest promotionSearchRequest, PromotionSearchResponse promotionSearchResponse) {
		return cacheTime;
	}
}
