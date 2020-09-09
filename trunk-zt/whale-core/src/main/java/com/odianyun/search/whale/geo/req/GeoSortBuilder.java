package com.odianyun.search.whale.geo.req;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortOrder;

import com.odianyun.search.whale.api.model.geo.GeoSearchRequest;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.index.api.common.MerchantAreaIndexFieldContants;

public class GeoSortBuilder implements GeoRequestBuilder{

	@Override
	public void build(ESSearchRequest esSearchRequest,
			GeoSearchRequest geoSearchRequest) {
		GeoDistanceSortBuilder geoDistanceSortBuilder=new GeoDistanceSortBuilder(MerchantAreaIndexFieldContants.LOCATION);
		geoDistanceSortBuilder.point(geoSearchRequest.getPoint().getLatitude(), geoSearchRequest.getPoint().getLongitude())
		.unit(DistanceUnit.KILOMETERS)
		.order(SortOrder.ASC);
		List<org.elasticsearch.search.sort.SortBuilder> sortBuilderList = new ArrayList
				<org.elasticsearch.search.sort.SortBuilder>() ;
		sortBuilderList.add(geoDistanceSortBuilder);
		esSearchRequest.setSortBuilderList(sortBuilderList);
	}

}
