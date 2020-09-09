package com.odianyun.search.whale.data.model;

import java.util.HashMap;
import java.util.Map;

public class MerchantSeries {
	private long id;
	private long merchant_id;
	private long main_merchant_product_id;
	
	public static final Map<String, String> resultMap = new HashMap<String, String>();
	
	static{
		resultMap.put("id","id");
	    resultMap.put("merchant_id","merchant_id");
	    resultMap.put("main_merchant_product_id","main_merchant_product_id");
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getMerchant_id() {
		return merchant_id;
	}
	public void setMerchant_id(long merchant_id) {
		this.merchant_id = merchant_id;
	}
	public long getMain_merchant_product_id() {
		return main_merchant_product_id;
	}
	public void setMain_merchant_product_id(long main_merchant_product_id) {
		this.main_merchant_product_id = main_merchant_product_id;
	}
	public static Map<String, String> getResultmap() {
		return resultMap;
	}
}
