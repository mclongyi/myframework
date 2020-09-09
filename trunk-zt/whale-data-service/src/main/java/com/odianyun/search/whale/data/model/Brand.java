package com.odianyun.search.whale.data.model;

import java.util.HashMap;
import java.util.Map;

public class Brand {
	
	private long id;
	
	private String name;
	
	private String chinese_name;
	
	private String english_name;
	
	private String brandLogo;
	
	public static final Map<String, String> resultMap = new HashMap<String, String>();
	
	static{
		resultMap.put("id", "id");
		resultMap.put("name", "name");
		resultMap.put("chinese_name", "chinese_name");
		resultMap.put("english_name", "english_name");
		resultMap.put("brandLogo", "log_url");
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getChinese_name() {
		return chinese_name;
	}

	public void setChinese_name(String chinese_name) {
		this.chinese_name = chinese_name;
	}

	public String getEnglish_name() {
		return english_name;
	}

	public void setEnglish_name(String english_name) {
		this.english_name = english_name;
	}

	public String getBrandLogo() {
		return brandLogo;
	}

	public void setBrandLogo(String brandLogo) {
		this.brandLogo = brandLogo;
	}

	@Override
	public String toString() {
		return "Brand [id=" + id + ", name=" + name + ", chinese_name="
				+ chinese_name + ", english_name=" + english_name + "]";
	}
	public static Map<String, String> getResultmap() {
		return resultMap;
	}
}
