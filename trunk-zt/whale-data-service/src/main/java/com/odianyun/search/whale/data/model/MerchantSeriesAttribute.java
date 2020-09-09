package com.odianyun.search.whale.data.model;

import java.util.HashMap;
import java.util.Map;

public class MerchantSeriesAttribute {

	//merchantSeriesId只有商家系列属性的时候用到，导购属性不会用到
	private Long merchantSeriesId;
	private long attrNameId;
    
    public static final Map<String, String> resultMap = new HashMap<String, String>();
	
	static{
		resultMap.put("merchantSeriesId", "merchantSeriesId");

		resultMap.put("attrNameId", "nameId");
	}
    
	public long getAttrNameId() {
		return attrNameId;
	}
	public void setAttrNameId(long attrNameId) {
		this.attrNameId = attrNameId;
	}
	/**
	 * @return the merchantSeriesId
	 */
	public Long getMerchantSeriesId() {
		return merchantSeriesId;
	}
	/**
	 * @param merchantSeriesId the merchantSeriesId to set
	 */
	public void setMerchantSeriesId(Long merchantSeriesId) {
		this.merchantSeriesId = merchantSeriesId;
	}
    
	public static Map<String, String> getResultmap() {
		return resultMap;
	}
    

}
