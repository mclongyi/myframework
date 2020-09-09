package com.odianyun.search.whale.o2o.req;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;

import com.odianyun.search.whale.api.model.o2o.O2OShopSearchRequest;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.index.api.common.IndexFieldConstants;

public class O2OShopSearchSortBuilder implements O2OShopSearchRequestBuilder{

	@Override
	public void build(ESSearchRequest esSearchRequest,
			O2OShopSearchRequest o2oShopSearchRequest) {
		List<org.elasticsearch.search.sort.SortBuilder> sortBuilderList = new ArrayList
				<org.elasticsearch.search.sort.SortBuilder>() ;
		sortBuilderList.add(new FieldSortBuilder(IndexFieldConstants.STOCK).order(SortOrder.DESC).missing(0));
		sortBuilderList.add(new FieldSortBuilder(IndexFieldConstants.SALETYPE).order(SortOrder.DESC).missing(1));
		esSearchRequest.setSortBuilderList(sortBuilderList);
	}

}
