package com.odianyun.search.whale.data.model;

import java.util.HashMap;
import java.util.Map;

public class MerchantProductPrice {
	
	private long merchant_product_id;
	
	private long product_id;
	
	private double merchant_product_price;
	
	public static final Map<String, String> resultMap = new HashMap<String, String>();
	
	static{
		resultMap.put("merchant_product_id", "merchant_product_id");
		resultMap.put("merchant_product_price", "merchant_product_price");
	}

	public long getMerchant_product_id() {
		return merchant_product_id;
	}

	public void setMerchant_product_id(long merchant_product_id) {
		this.merchant_product_id = merchant_product_id;
	}

	public long getProduct_id() {
		return product_id;
	}

	public void setProduct_id(long product_id) {
		this.product_id = product_id;
	}

	public double getMerchant_product_price() {
		return merchant_product_price;
	}

	public void setMerchant_product_price(double merchant_product_price) {
		this.merchant_product_price = merchant_product_price;
	}

	@Override
	public String toString() {
		return "MerchantProductPrice [merchant_product_id="
				+ merchant_product_id + ", product_id=" + product_id
				+ ", merchant_product_price=" + merchant_product_price + "]";
	}
	public static Map<String, String> getResultmap() {
		return resultMap;
	}

}
