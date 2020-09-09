package com.odianyun.search.whale.data.model.suggest;


import java.util.List;

public class SuggestBusinessWord {
	
	private String keyword;
	
	private String payload;
	
	private Integer frequency = 1;
	
	private Integer type = 0;
	
	private Integer companyId;

	private List<Long> merchantIdList;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public List<Long> getMerchantIdList() {
		return merchantIdList;
	}

	public void setMerchantIdList(List<Long> merchantIdList) {
		this.merchantIdList = merchantIdList;
	}
}
