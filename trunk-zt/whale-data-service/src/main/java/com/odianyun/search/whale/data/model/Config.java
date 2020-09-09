package com.odianyun.search.whale.data.model;

import java.util.HashMap;
import java.util.Map;

public class Config {
	
	private String key;
	
	private String value;
	
	private String poolName;
	
	private Long companyId;
	
	public static final Map<String, String> resultMap = new HashMap<String, String>();
	
	static{
		resultMap.put("key","config_key" );
        resultMap.put("value","config_value");
        resultMap.put("poolName","pool_name");
        resultMap.put("companyId","company_id");        
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getPoolName() {
		return poolName;
	}

	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public static Map<String, String> getResultmap() {
		return resultMap;
	}
	
	
}
