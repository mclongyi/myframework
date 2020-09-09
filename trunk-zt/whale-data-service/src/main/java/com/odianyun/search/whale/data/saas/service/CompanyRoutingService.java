package com.odianyun.search.whale.data.saas.service;

import com.odianyun.search.whale.data.saas.model.CompanyAppType;
import com.odianyun.search.whale.data.saas.model.ESClusterConfig;

public interface CompanyRoutingService {
	/**
	 * 根据companyId和CompanyAppType(b2c/o2o)获取相应的es集群信息
	 * @param companyId
	 * @param companyAppType
	 * @return
	 */
	public ESClusterConfig getCompanyESClusterConfig(Integer companyId, CompanyAppType companyAppType);
	/**
	 * 根据companyId和CompanyAppType(b2c/o2o)获取相应的索引别名
	 * @param companyId
	 * @param companyAppType
	 * @return
	 */
	public String getCompanyIndexAliasName(Integer companyId, CompanyAppType companyAppType);
	/**
	 * 根据companyId和CompanyAppType(b2c/o2o)获取相应的索引名前缀
	 * @param companyId
	 * @param companyAppType
	 * @return
	 */
	public String getCompanyIndexPreName(Integer companyId, CompanyAppType companyAppType);
	
}
