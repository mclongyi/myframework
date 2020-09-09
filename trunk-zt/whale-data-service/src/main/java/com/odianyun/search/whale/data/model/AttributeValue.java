package com.odianyun.search.whale.data.model;

import java.util.HashMap;
import java.util.Map;

public class AttributeValue {
	//merchantSeriesId只有商家系列属性的时候用到，导购属性不会用到
	private Long merchantSeriesId;
	private long attrNameId;
	private String attrName;
	private Integer attrType=0;
    private long attrValueId;
    private String attrValue;
    private Long parentId;
    private Integer sortValue=999;
    
    public static final Map<String, String> resultMap = new HashMap<String, String>();
	
	static{
		resultMap.put("merchantSeriesId", "merchantSeriesId");
		resultMap.put("attrValueId", "valueId");
		resultMap.put("attrValue", "value");
		resultMap.put("attrNameId", "nameId");
		resultMap.put("attrName", "name_name");
		resultMap.put("parentId", "parent_id");
		resultMap.put("sortValue", "sort_value");
		resultMap.put("attrType", "type");
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
	public String getAttrName() {
		return attrName;
	}
	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}
	public String getAttrValue() {
		return attrValue;
	}
	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}
	
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public Integer getSortValue() {
		return sortValue;
	}
	public void setSortValue(Integer sortValue) {
		this.sortValue = sortValue;
	}

	@Override
	public String toString() {
		return "AttributeValue [attrNameId=" + attrNameId + ", attrName="
				+ attrName + ", attrValueId=" + attrValueId + ", attrValue="
				+ attrValue + "]";
	}
	public Integer getAttrType() {
		return attrType;
	}
	public void setAttrType(Integer attrType) {
		this.attrType = attrType;
	}
	public Long getMerchantSeriesId() {
		return merchantSeriesId;
	}
	public void setMerchantSeriesId(Long merchantSeriesId) {
		this.merchantSeriesId = merchantSeriesId;
	}
    
	public static Map<String, String> getResultmap() {
		return resultMap;
	}
    

}
