package com.odianyun.search.whale.store.zk.model;

public class VersionData {
	
	private String version;
	
	private Long count;
	
	private Boolean isSuccess;
	
	private String message;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public Boolean getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "VersionData [version=" + version + ", count=" + count + ", isSuccess=" + isSuccess + ", message="
				+ message + "]";
	}
	

}
