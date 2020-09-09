package com.odianyun.search.whale.data.model;

import java.util.HashMap;
import java.util.Map;

public class MerchantProductVolume4Sale {
	
	private long merchant_product_id;
	
	private long product_id=0;
	
	private Integer volume4sales=0;
	
	public static final Map<String, String> resultMap = new HashMap<String, String>();
	
	static{
		resultMap.put("merchant_product_id", "merchant_prod_id");
		resultMap.put("product_id", "product_id");
		resultMap.put("volume4sales", "cumulative_sales_volume");
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

	public Integer getVolume4sales() {
		return volume4sales;
	}

	public void setVolume4sales(Integer volume4sales) {
		this.volume4sales = volume4sales;
	}

	@Override
	public String toString() {
		return "MerchantProductVolume4Sale [merchant_product_id="
				+ merchant_product_id + ", product_id=" + product_id
				+ ", volume4sales=" + volume4sales + "]";
	}
	public static Map<String, String> getResultmap() {
		return resultMap;
	}
}
