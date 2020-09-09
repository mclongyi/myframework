package com.odianyun.search.whale.data.model;

import java.util.HashMap;
import java.util.Map;

public class MerchantProductSimple {
	
	private long id;

	private long merchant_id;

	private long company_id=0;
	
	private Integer managementState=0;

	private Integer status=0;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getMerchant_id() {
		return merchant_id;
	}

	public void setMerchant_id(long merchant_id) {
		this.merchant_id = merchant_id;
	}

	public long getCompany_id() {
		return company_id;
	}

	public void setCompany_id(long company_id) {
		this.company_id = company_id;
	}

	public Integer getManagementState() {
		return managementState;
	}

	public void setManagementState(Integer managementState) {
		this.managementState = managementState;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
