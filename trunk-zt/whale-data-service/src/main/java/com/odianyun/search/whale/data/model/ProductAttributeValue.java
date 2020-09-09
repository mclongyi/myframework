package com.odianyun.search.whale.data.model;

import java.util.HashMap;
import java.util.Map;

public class ProductAttributeValue {
	
	private long productId;
	private long attrNameId;
    private long attrValueId;
    
    public static final Map<String, String> resultMap = new HashMap<String, String>();
	
	static{
		resultMap.put("productId", "product_id");
		resultMap.put("attrNameId", "att_name_id");
		resultMap.put("attrValueId", "att_value_id");
	}
	
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public long getAttrNameId() {
		return attrNameId;
	}
	public void setAttrNameId(long attrNameId) {
		this.attrNameId = attrNameId;
	}
	public long getAttrValueId() {
		return attrValueId;
	}
	public void setAttrValueId(long attrValueId) {
		this.attrValueId = attrValueId;
	}
	@Override
	public String toString() {
		return "ProductAttributeValue [productId=" + productId
				+ ", attrNameId=" + attrNameId + ", attrValueId=" + attrValueId
				+ "]";
	}
	public static Map<String, String> getResultmap() {
		return resultMap;
	}
    

}
