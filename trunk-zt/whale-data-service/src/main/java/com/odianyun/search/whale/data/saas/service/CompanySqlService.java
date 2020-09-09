package com.odianyun.search.whale.data.saas.service;

public interface CompanySqlService {
	/**
	 * 根据companyId和sqlname获取相应的sql
	 * @param companyId
	 * @param sqlName
	 * @return
	 */
	public String getSql(Integer companyId, String sqlName);
	/**
	 * 根据companyId和sqlname获取相应的sql 暂不使用
	 * @param companyId
	 * @param sqlName
	 * @param parameterObject 是sql的参数
	 * @return
	 */
	public String getSql(Integer companyId, String sqlName, Object parameterObject);

}
