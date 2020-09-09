package com.odianyun.search.whale.api.model.o2o;

import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.o2o.O2OShopSearchRequest;
import com.odianyun.search.whale.api.model.o2o.O2OShopSearchResponse;
import com.odianyun.soa.InputDTO;
import com.odianyun.soa.OutputDTO;

public interface O2OShopSearchService {
	
	//店铺内搜索
	@Deprecated
	public O2OShopSearchResponse shopSearch(O2OShopSearchRequest o2OShopSearchRequest) throws SearchException;

	public OutputDTO<O2OShopSearchResponse> shopSearchStandard(InputDTO<O2OShopSearchRequest> inputDTO) throws SearchException;

}
