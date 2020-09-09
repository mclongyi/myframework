package com.odianyun.search.whale.data.model;

import java.util.HashMap;
import java.util.Map;

public class MerchantProductPic {
	
	private Long id;
	
	private String url;
	
	private Integer sort_value=0;
	
	public static final Map<String, String> resultMap = new HashMap<String, String>();
	
	static{
		resultMap.put("id","id");
		resultMap.put("url","url");
		resultMap.put("sort_value","sort_value");
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getSort_value() {
		return sort_value;
	}

	public void setSort_value(Integer sort_value) {
		this.sort_value = sort_value;
	}
	
	public static Map<String, String> getResultmap() {
		return resultMap;
	}
	

}
