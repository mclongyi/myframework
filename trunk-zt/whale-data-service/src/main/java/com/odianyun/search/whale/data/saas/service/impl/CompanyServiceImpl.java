package com.odianyun.search.whale.data.saas.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.common.util.ConfigUtil;
import com.odianyun.search.whale.data.common.ServiceConstants;
import com.odianyun.search.whale.data.dao.MerchantProductDao;
import com.odianyun.search.whale.data.manager.CompanyDBCacheManager;
import com.odianyun.search.whale.data.saas.dao.CompanyDao;
import com.odianyun.search.whale.data.saas.model.Company;
import com.odianyun.search.whale.data.saas.service.CompanyService;
import com.odianyun.search.whale.data.service.impl.AbstractDBService;

public class CompanyServiceImpl extends AbstractDBService implements CompanyService{
	private static Logger log = Logger.getLogger(CompanyServiceImpl.class);
	
	@Autowired
	MerchantProductDao merchantProductDao;

	@Override
	protected void tryReload() throws Exception {
		List<Integer> companyIds=getAllCompanyIds();
		if(CollectionUtils.isNotEmpty(companyIds)){
			CompanyDBCacheManager.instance.setCompanyIds(companyIds);
		}

	}

	@Override
	protected void tryReload(List<Long> ids) throws Exception {
		
	}

	@Override
	public int getInterval() {
		return 10;
	}

	@Override
	public List<Integer> getAllCompanyIds() {
		List<Integer> companyIds = new ArrayList<>();
		try {
			companyIds.addAll(merchantProductDao.queryCompanyIds());
		} catch (Exception e) {
			log.error("getAllCompanyIds error !!!  " + e.getMessage());
		}
		return companyIds;
	}

}
