package com.odianyun.search.whale.data.model.history;

import java.util.HashMap;
import java.util.Map;

public class LogWord {
	
	private String keyword;
	
	private int frequency;
	
	private int resultCount;
	
	private String userId;
	
	private int type;
	
	private int companyId;
	
	public static final Map<String, String> resultMap = new HashMap<String, String>();
	
	static{
		resultMap.put("keyword","keyword" );
        resultMap.put("frequency","frequency" );
        resultMap.put("resultCount","result_count" );
        resultMap.put("userId","user_id" );
        resultMap.put("type","type" );
        resultMap.put("companyId","company_id" );
	}


	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public int getResultCount() {
		return resultCount;
	}

	public void setResultCount(int resultCount) {
		this.resultCount = resultCount;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
	public static Map<String, String> getResultmap() {
		return resultMap;
	}
	
}
