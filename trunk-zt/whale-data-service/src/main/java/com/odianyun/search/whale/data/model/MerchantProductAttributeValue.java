package com.odianyun.search.whale.data.model;

import java.util.HashMap;
import java.util.Map;

public class MerchantProductAttributeValue {

	private Long merchantProductId;

	private Long attrValueId;

	private Integer attrType;
		
	public static final Map<String, String> resultMap = new HashMap<String, String>();
	
	static{
		resultMap.put("merchantProductId","merchant_product_id");
	    resultMap.put("attrValueId","att_value_id");
		resultMap.put("attrType","attr_type");
	}

	public Long getMerchantProductId() {
		return merchantProductId;
	}

	public void setMerchantProductId(Long merchantProductId) {
		this.merchantProductId = merchantProductId;
	}

	public Long getAttrValueId() {
		return attrValueId;
	}

	public void setAttrValueId(Long attrValueId) {
		this.attrValueId = attrValueId;
	}

	public Integer getAttrType() {
		return attrType;
	}

	public void setAttrType(Integer attrType) {
		this.attrType = attrType;
	}

	public static Map<String, String> getResultmap() {
		return resultMap;
	}
}
