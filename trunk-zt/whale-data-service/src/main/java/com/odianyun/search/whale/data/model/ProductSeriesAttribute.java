package com.odianyun.search.whale.data.model;

import java.util.HashMap;
import java.util.Map;

public class ProductSeriesAttribute {

	//merchantSeriesId只有商家系列属性的时候用到，导购属性不会用到
	private Long virtualMerchantProductId;
	private long attrNameId;
    
    public static final Map<String, String> resultMap = new HashMap<String, String>();
	
	static{
		resultMap.put("virtualMerchantProductId", "virtualMerchantProductId");

		resultMap.put("attrNameId", "nameId");
	}
    
	public long getAttrNameId() {
		return attrNameId;
	}
	public void setAttrNameId(long attrNameId) {
		this.attrNameId = attrNameId;
	}
	public static Map<String, String> getResultmap() {
		return resultMap;
	}

	public Long getVirtualMerchantProductId() {
		return virtualMerchantProductId;
	}

	public void setVirtualMerchantProductId(Long virtualMerchantProductId) {
		this.virtualMerchantProductId = virtualMerchantProductId;
	}
}
