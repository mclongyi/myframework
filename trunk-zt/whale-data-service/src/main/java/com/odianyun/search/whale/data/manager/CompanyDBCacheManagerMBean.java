package com.odianyun.search.whale.data.manager;

import java.util.List;

public interface CompanyDBCacheManagerMBean {
	
	public void reloadAll();
	
	public void reload(int companyId);
	
	/**
	 * AbstractDBService subClass's fullpath
	 * @param name
	 */
	public void reload(String name,int companyId);

	public void reload(String name, List<Long> ids,int companyId);

}
