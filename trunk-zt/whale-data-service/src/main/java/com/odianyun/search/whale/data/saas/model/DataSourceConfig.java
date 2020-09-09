package com.odianyun.search.whale.data.saas.model;

public class DataSourceConfig {
	//公司id
	private int companyId;
	//数据库类型:product,stock,search
	private String dbType;
	//数据库用户名
	private String username;
	//数据库密码
	private String password;
	//数据库连接url
	private String jdbcUrl;
	
	public int getCompanyId() {
		return companyId;
	}
	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
	public String getDbType() {
		return dbType;
	}
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getJdbcUrl() {
		return jdbcUrl;
	}
	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}
	@Override
	public String toString() {
		return "DataSourceConfig [companyId=" + companyId + ", dbType="
				+ dbType + ", username=" + username + ", password=" + password
				+ ", jdbcUrl=" + jdbcUrl + "]";
	}
	
	
	
	
}
