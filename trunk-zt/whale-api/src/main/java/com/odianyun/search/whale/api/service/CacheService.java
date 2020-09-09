package com.odianyun.search.whale.api.service;

import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.req.ReloadCacheRequest;
import com.odianyun.soa.InputDTO;

public interface CacheService {

	@Deprecated
	void reloadCache(String name,int companyId) throws SearchException;

	void reloadCacheStandard(InputDTO<ReloadCacheRequest> inputDTO) throws SearchException;
}
