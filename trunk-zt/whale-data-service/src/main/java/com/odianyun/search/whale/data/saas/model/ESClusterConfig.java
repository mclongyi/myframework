package com.odianyun.search.whale.data.saas.model;

public class ESClusterConfig {
	private Integer id;
	
	private String clusterName;
	
	private String clusterNode;
	
	private String adminUrl;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	public String getClusterNode() {
		return clusterNode;
	}

	public void setClusterNode(String clusterNode) {
		this.clusterNode = clusterNode;
	}

	public String getAdminUrl() {
		return adminUrl;
	}

	public void setAdminUrl(String adminUrl) {
		this.adminUrl = adminUrl;
	}

	@Override
	public String toString() {
		return "ESClusterConfig [id=" + id + ", clusterName=" + clusterName + ", clusterNode="
				+ clusterNode + "]";
	}
}
