package com.odianyun.search.whale.data.saas.model;

import com.odianyun.search.whale.index.api.common.IndexConstants;

public class CommonConfig {
	private Integer id;
	
	private Integer companyId;
	
	private String indexName;
	
	private String indexType = IndexConstants.index_type;
	
	private Integer esClusterId;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public String getIndexType() {
		return indexType;
	}

	public void setIndexType(String indexType) {
		this.indexType = indexType;
	}

	public Integer getEsClusterId() {
		return esClusterId;
	}

	public void setEsClusterId(Integer esClusterId) {
		this.esClusterId = esClusterId;
	}
	
	@Override
	public String toString() {
		return "CommonConfig [id=" + id + ", companyId=" + companyId + ", indexName=" + indexName
				+ ", indexType=" + indexType + ", esClusterId=" + esClusterId + "]";
	}
}
