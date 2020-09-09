package com.odianyun.search.whale.data.saas.service;

import com.odianyun.search.whale.data.saas.dao.BaseDao;
import com.odianyun.search.whale.data.saas.model.DBType;

/**
 * 获取对应的BaseDao,每个BaseDao一直会缓存起来
 * @author fishcus
 *
 */
public interface BaseDaoService {
	
	public BaseDao getBaseDao(int companyId,DBType dbType);
	
}
