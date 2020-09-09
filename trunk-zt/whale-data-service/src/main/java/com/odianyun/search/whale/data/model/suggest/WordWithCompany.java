package com.odianyun.search.whale.data.model.suggest;

import java.util.HashMap;
import java.util.Map;

public class WordWithCompany {
	
	private Long id;
	
	private String keyword;
	
	private Integer companyId;
	
	private Integer frequency;
	
	public static final Map<String, String> resultMap = new HashMap<String, String>();
	
	static{
		resultMap.put("id","id" );
		resultMap.put("keyword","keyword" );
    	resultMap.put("companyId","company_id" );
    	resultMap.put("frequency","frequency" );
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}
	
	public static Map<String, String> getResultmap() {
		return resultMap;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
