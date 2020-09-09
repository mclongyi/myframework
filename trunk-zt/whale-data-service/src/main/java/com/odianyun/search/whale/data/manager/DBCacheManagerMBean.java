package com.odianyun.search.whale.data.manager;

/**
 * MBean接口
 *
 * @author ody
 *
 */
public interface DBCacheManagerMBean {

	void reloadAll();

	/**
	 * AbstractDBService subClass's fullpath
	 * @param name
	 */
	void reload(String name);
}
