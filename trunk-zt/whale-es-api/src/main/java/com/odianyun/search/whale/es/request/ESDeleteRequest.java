package com.odianyun.search.whale.es.request;

import java.util.List;

public class ESDeleteRequest {
	
	private String indexName;
	
	private String type;
	
	private List<String> ids;
	
	public ESDeleteRequest(String indexName, String type, List<String> ids){
		super();
		this.indexName = indexName;
		this.type = type;
		this.ids = ids;
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

	public List<String> getIds() {
		return ids;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}
	
}
