package com.odianyun.search.whale.es.request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ESSuggestRequest {
	
    private String indexName;
	
	private String type;
	
	private String input;
	
	private String field;
	
	private int start=0;
	
	private int count=10;

	private Map<String,List<String>> categoryMap = new HashMap<>();

	public ESSuggestRequest(String indexName, String type){
		this(indexName,type,null);
	}

	public ESSuggestRequest(String indexName, String type,
			String input) {
		super();
		this.indexName = indexName;
		this.type = type;
		this.input = input;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Map<String, List<String>> getCategoryMap() {
		return categoryMap;
	}

	public void setCategoryMap(Map<String, List<String>> categoryMap) {
		this.categoryMap = categoryMap;
	}
}
