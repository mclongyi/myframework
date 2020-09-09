package com.odianyun.search.whale.es.request;

import java.util.Map;

public class ESIndexRequest {
	
	private String indexName;
	
	private String type;
	
	private String id;
	
	private Map<String,Object> data;
	
	private String jsonData;

	//指定父文档
	private String parent;

	public ESIndexRequest(String indexName, String type, String id,
			Map<String, Object> data) {
		super();
		this.indexName = indexName;
		this.type = type;
		this.id = id;
		this.data = data;
	}
	
	public ESIndexRequest(String indexName, String type, String id,
			String jsonData) {
		super();
		this.indexName = indexName;
		this.type = type;
		this.id = id;
		this.jsonData = jsonData;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ESIndexRequest that = (ESIndexRequest) o;

		if (indexName != null ? !indexName.equals(that.indexName) : that.indexName != null) return false;
		if (type != null ? !type.equals(that.type) : that.type != null) return false;
		return !(id != null ? !id.equals(that.id) : that.id != null);

	}

	@Override
	public int hashCode() {
		int result = indexName != null ? indexName.hashCode() : 0;
		result = 31 * result + (type != null ? type.hashCode() : 0);
		result = 31 * result + (id != null ? id.hashCode() : 0);
		return result;
	}
}
