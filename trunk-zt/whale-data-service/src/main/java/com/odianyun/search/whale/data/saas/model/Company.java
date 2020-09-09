package com.odianyun.search.whale.data.saas.model;

public class Company {
	//companyId
	private Integer id;
	//公司名称
	private String name;
	//公司简称，eg:yh,zs,gsh
	private String shortName;
	//是否是虚拟company,这个字段可以不保留，直接判断virtualCompanyId是否为空或者为0
	//private Boolean isVirtual;
	//对应的虚拟companyId
	private Integer virtualCompanyId;
	//company所属的组
	private String groupName;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
//	public Boolean getIsVirtual() {
//		return isVirtual;
//	}
//	public void setIsVirtual(Boolean isVirtual) {
//		this.isVirtual = isVirtual;
//	}
	public Integer getVirtualCompanyId() {
		return virtualCompanyId;
	}
	public void setVirtualCompanyId(Integer virtualCompanyId) {
		this.virtualCompanyId = virtualCompanyId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	@Override
	public String toString() {
		return "Company [id=" + id + ", name=" + name + ", shortName=" + shortName + ", virtualCompanyId="
				+ virtualCompanyId + ", groupName=" + groupName + "]";
	}

}
