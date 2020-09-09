package com.odianyun.search.whale.data.model;

import java.util.HashMap;
import java.util.Map;

public class MerchantProductStock {
	
	private Long merchant_product_id;
	
	private Long product_id;
	
	private Long merchant_id;
	
	private Long real_stock_num=0l;
	
	private Long real_frozen_stock_num=0l;

	public static final Map<String, String> resultMap = new HashMap<String, String>();
	
	static{
		resultMap.put("merchant_product_id","merchant_product_id");
        resultMap.put("product_id","product_id");
        resultMap.put("merchant_id","merchant_id");
        resultMap.put("real_stock_num","real_stock_num");
        resultMap.put("real_frozen_stock_num","real_frozen_stock_num");
	}
	public Long getMerchant_product_id() {
		return merchant_product_id;
	}


	public void setMerchant_product_id(Long merchant_product_id) {
		this.merchant_product_id = merchant_product_id;
	}


	public Long getProduct_id() {
		return product_id;
	}


	public void setProduct_id(Long product_id) {
		this.product_id = product_id;
	}


	public Long getMerchant_id() {
		return merchant_id;
	}


	public void setMerchant_id(Long merchant_id) {
		this.merchant_id = merchant_id;
	}


	public Long getReal_stock_num() {
		return real_stock_num;
	}


	public void setReal_stock_num(Long real_stock_num) {
		this.real_stock_num = real_stock_num;
	}


	public Long getReal_frozen_stock_num() {
		return real_frozen_stock_num;
	}


	public void setReal_frozen_stock_num(Long real_frozen_stock_num) {
		this.real_frozen_stock_num = real_frozen_stock_num;
	}


	@Override
	public String toString(){
		return "MerchantProductStock  [merchant_product_id="+ merchant_product_id
				+", product_id="+product_id+", merchant_id="+merchant_id +", real_stock_num="
				+ real_stock_num +", real_frozen_stock_num="+real_frozen_stock_num+"]";
	}
	public static Map<String, String> getResultmap() {
		return resultMap;
	}
}
