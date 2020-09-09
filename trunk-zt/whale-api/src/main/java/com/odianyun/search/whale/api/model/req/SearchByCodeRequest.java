package com.odianyun.search.whale.api.model.req;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SearchByCodeRequest implements Serializable{

	private static final long serialVersionUID = -2575828692563059292L;

	public SearchByCodeRequest() {
	}

	private List<String> mpCodes;
	
	private Integer companyId;
	
	public SearchByCodeRequest(List<String> mpCodes, Integer companyId){
		this.mpCodes = mpCodes;
		this.companyId = companyId;
	}
	/**
	 * @return the mpCodes
	 */
	public List<String> getMpCodes() {
		return mpCodes;
	}

	/**
	 * @param mpCodes the mpCodes to set
	 */
	public void setMpCodes(List<String> mpCodes) {
		this.mpCodes = mpCodes;
	}

	/**
	 * @return the companyId
	 */
	public Integer getCompanyId() {
		return companyId;
	}

	/**
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	
}
