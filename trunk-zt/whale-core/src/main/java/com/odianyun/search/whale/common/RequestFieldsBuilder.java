package com.odianyun.search.whale.common;

import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.index.api.common.IndexFieldConstants;

import java.util.ArrayList;
import java.util.List;

public class RequestFieldsBuilder {
	
	public static void build(ESSearchRequest esSearchRequest){
		List<String> fields = buildFields();
		esSearchRequest.setFields(fields);
	}

	public static void innerBuild(ESSearchRequest esSearchRequest){
		List<String> fields = buildInnerFields();
		esSearchRequest.setFields(fields);
	}

	public static List<String> buildFields(){
		List<String> fields=new ArrayList<String>();
		fields.add(IndexFieldConstants.ID);
		fields.add(IndexFieldConstants.MERCHANTID);
		fields.add(IndexFieldConstants.PRODUCT_ID);
		fields.add(IndexFieldConstants.PRICE);
		fields.add(IndexFieldConstants.STOCK);
		fields.add(IndexFieldConstants.PICURL);
		fields.add(IndexFieldConstants.PRODUCTNAME);
		fields.add(IndexFieldConstants.TAX);
		fields.add(IndexFieldConstants.TYPE);
		fields.add(IndexFieldConstants.MERCHANTSERIESID);
		fields.add(IndexFieldConstants.COMPANYID);
		fields.add(IndexFieldConstants.CATEGORYID);
		fields.add(IndexFieldConstants.CALCULATION_UNIT);
		fields.add(IndexFieldConstants.STANDARD);
		fields.add(IndexFieldConstants.SALETYPE);
		fields.add(IndexFieldConstants.BRANDID_SEARCH);
		fields.add(IndexFieldConstants.CODE);
		fields.add(IndexFieldConstants.EAN_NO);
		fields.add(IndexFieldConstants.PRODUCT_CODE);
		fields.add(IndexFieldConstants.UPDATE_TIME);

		fields.add(IndexFieldConstants.VOLUME4SALE);
		fields.add(IndexFieldConstants.REAL_VOLUME4SALE);
		fields.add(IndexFieldConstants.RATE);
		fields.add(IndexFieldConstants.POSITIVE_RATE);
		fields.add(IndexFieldConstants.RATING_COUNT);
		fields.add(IndexFieldConstants.TYPE_OF_PRODUCT);
		fields.add(IndexFieldConstants.SCRIPT_IDS);
		fields.add(IndexFieldConstants.THIRD_CODE);
		
		fields.add(IndexFieldConstants.MIN_SIZE);
		fields.add(IndexFieldConstants.MAX_SIZE);
		fields.add(IndexFieldConstants.PLACE_OF_ORIGIN);
		fields.add(IndexFieldConstants.PLACE_OF_ORIGIN_LOGO);
		fields.add(IndexFieldConstants.SUBTITLE);
		fields.add(IndexFieldConstants.CARD_TYPE);
		fields.add(IndexFieldConstants.CARD_ID);
		return fields;
	}

	public static List<String> buildInnerFields(){
		List<String> fields=new ArrayList<String>();
		fields.add(IndexFieldConstants.ID);
		fields.add(IndexFieldConstants.PRICE);
		fields.add(IndexFieldConstants.PICURL);
		fields.add(IndexFieldConstants.COMPANYID);
		fields.add(IndexFieldConstants.MERCHANT_CATEGORYID);
		fields.add(IndexFieldConstants.MERCHANTID);
		fields.add(IndexFieldConstants.PRODUCTNAME);

		return fields;
	}


}
