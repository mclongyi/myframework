package com.odianyun.search.whale.index.api.model.req;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
public class HistoryWriteRequest implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HistoryWriteRequest(){}
	public HistoryWriteRequest(Integer companyId, String userId, String keyword, HistoryType type) {
		this.keyword = keyword;
		this.type = type;
		this.companyId = companyId;
		this.userId = userId;
	}
	
	public HistoryWriteRequest(Integer companyId, String userId, String keyword) {
		this(companyId,userId,keyword,HistoryType.SEARCH);
	}
	//用户搜索关键词
	private String keyword;
	//历史记录类型
	private HistoryType type;
	//店铺内搜索历史
	private Long merchantId;
	
	private Integer companyId;
	
	private String userId;
	
	private int frequency = 1;
	
	private int resultCount;


	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public HistoryType getType() {
		return type;
	}

	public void setType(HistoryType type) {
		this.type = type;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
}
