package com.odianyun.search.whale.data.saas.model;

public class DumpConfig {
	private String indexName;
	
	private Integer jobType;
	
	private Integer period;
	
	private String cronExpression;

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public Integer getJobType() {
		return jobType;
	}

	public void setJobType(Integer jobType) {
		this.jobType = jobType;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	@Override
	public String toString() {
		return "DumpConfig [indexName=" + indexName + ", jobType=" + jobType + ", period=" + period
				+ ", cronExpression=" + cronExpression + "]";
	}
}
