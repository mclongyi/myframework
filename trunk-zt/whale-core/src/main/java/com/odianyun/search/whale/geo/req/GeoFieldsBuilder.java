package com.odianyun.search.whale.geo.req;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.odianyun.search.whale.api.model.geo.GeoSearchRequest;
import com.odianyun.search.whale.api.model.req.BaseSearchRequest;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.index.api.common.IndexFieldConstants;
import com.odianyun.search.whale.index.api.common.MerchantAreaIndexFieldContants;

public class GeoFieldsBuilder implements GeoRequestBuilder {

	@Override
	public void build(ESSearchRequest esSearchRequest, GeoSearchRequest searchRequest) {
		List<String> fields=new ArrayList<String>();
		fields.add(MerchantAreaIndexFieldContants.MERCHANTID);
		fields.add(MerchantAreaIndexFieldContants.LOCATION);

		//外卖增加merchantType
		fields.add(MerchantAreaIndexFieldContants.MERCHANTTYPE);
		
		esSearchRequest.setFields(fields);
//		esSearchRequest.setStart(0);
//		esSearchRequest.setCount(searchRequest.getNum());
		esSearchRequest.setStart(searchRequest.getStart());
		esSearchRequest.setCount(searchRequest.getCount());
	}

}
