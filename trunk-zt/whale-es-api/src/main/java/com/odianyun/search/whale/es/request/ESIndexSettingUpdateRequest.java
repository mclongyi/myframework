package com.odianyun.search.whale.es.request;

import java.util.Map;

public class ESIndexSettingUpdateRequest {

	private String indexName;
	
	private String type;
	
	private Map<String,String> data;
	
	private String jsonData;

	public ESIndexSettingUpdateRequest(String indexName,
			Map<String, String> data) {
		super();
		this.indexName = indexName;
		this.data = data;
	}
	
	public ESIndexSettingUpdateRequest(String indexName, 
			String jsonData) {
		super();
		this.indexName = indexName;
		this.jsonData = jsonData;
	}
	
	/**
	 * @return the indexName
	 */
	public String getIndexName() {
		return indexName;
	}

	/**
	 * @param indexName the indexName to set
	 */
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the data
	 */
	public Map<String, String> getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Map<String, String> data) {
		this.data = data;
	}

	/**
	 * @return the jsonData
	 */
	public String getJsonData() {
		return jsonData;
	}

	/**
	 * @param jsonData the jsonData to set
	 */
	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

}
