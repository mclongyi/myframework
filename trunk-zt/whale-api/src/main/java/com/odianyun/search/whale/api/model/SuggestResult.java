package com.odianyun.search.whale.api.model;

import java.util.Map;

public class SuggestResult {
	private String keyword;
	private int count;
	private Map attachedInfo;
	
	public SuggestResult(String keyword,int count){
		this.keyword = keyword;
		this.count = count;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Map getAttachedInfo() {
		return attachedInfo;
	}
	public void setAttachedInfo(Map attachedInfo) {
		this.attachedInfo = attachedInfo;
	}
	
	
}
