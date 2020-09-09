package com.odianyun.search.whale.es.request;

public class ESSpellCheckRequest {

    private String indexName;
	private String type;
	private String input;
	private String field;
	
	public ESSpellCheckRequest(String indexName,String type){
		this.indexName = indexName;
		this.type = type;
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
}
