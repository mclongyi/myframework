package com.odianyun.search.whale.index.api.common;

import java.io.Serializable;
import java.util.List;

import com.odianyun.search.whale.index.api.common.UpdateType;

public class UpdateMessage implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5133239984634233524L;

	private List<Long> ids;
	
	private UpdateType updateType;
	
	private int companyId;

	private String version;
	
	public UpdateMessage(){}
	
	public UpdateMessage(List<Long> ids, UpdateType updateType,int companyId){
		this.ids = ids;
		this.updateType = updateType;
		this.companyId=companyId;
	}

	public List<Long> getIds() {
		return ids;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}

	public UpdateType getUpdateType() {
		return updateType;
	}

	public void setUpdateType(UpdateType updateType) {
		this.updateType = updateType;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
