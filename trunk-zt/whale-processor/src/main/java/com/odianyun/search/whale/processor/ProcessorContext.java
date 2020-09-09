package com.odianyun.search.whale.processor;

import java.util.List;

public class ProcessorContext {

	
	private List<DataRecord> dataRecords;
	
	//是否结束标记
	private Boolean endSignal=false;
	
	String indexName;
	
	String indexType;
	
	int companyId;
	
	public ProcessorContext(){
		
	}
	
	public ProcessorContext(List<DataRecord> dataRecords) {
		this.dataRecords = dataRecords;
	}

	public List<DataRecord> getDataRecords() {
		return dataRecords;
	}

	public void setDataRecords(List<DataRecord> dataRecords) {
		this.dataRecords = dataRecords;
	}

	public Boolean getEndSignal() {
		return endSignal;
	}

	public void setEndSignal(Boolean endSignal) {
		this.endSignal = endSignal;
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

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

}
