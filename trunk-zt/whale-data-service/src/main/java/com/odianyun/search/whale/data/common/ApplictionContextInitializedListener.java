package com.odianyun.search.whale.data.common;

import java.util.ArrayList;
import java.util.List;

import com.odianyun.search.whale.data.service.HotSaleMerchantProductService;
import org.apache.log4j.Logger;
import org.elasticsearch.common.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.odianyun.search.whale.common.util.ConfigUtil;
import com.odianyun.search.whale.data.manager.CompanyDBCacheManager;
import com.odianyun.search.whale.data.manager.DBCacheManager;
import com.odianyun.search.whale.data.saas.service.CompanyService;

public class ApplictionContextInitializedListener implements
       ApplicationListener<ContextRefreshedEvent>{

	static Logger logger = Logger.getLogger(ApplictionContextInitializedListener.class);
	
	@Autowired
	CompanyService companyService;
	@Autowired
	HotSaleMerchantProductService hotSaleMerchantProductService;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		logger.info(event.getApplicationContext().getDisplayName() +" loaded !!!!!!!!!!!");
		if(event.getApplicationContext().getParent() == null && 
				event.getSource() instanceof XmlWebApplicationContext && 
				event.getApplicationContext().getDisplayName().equals("Root WebApplicationContext")){
			logger.info("ApplictionContextInitializedListener linster ApplictionContext Initialized");
			boolean isStartWithSpark = ConfigUtil.getBool("isStartWithSpark", false);
			String poolName = System.getProperty(ServiceConstants.POOL_NAME,"index");
			DBCacheManager.instance.init();
			// isStartWithSpark为true且是index时不需要加载CompanyDBCacche,本地也不用启动consumer
			if(isStartWithSpark && StringUtils.isNotBlank(poolName) && poolName.equals("index")){
				return;
			}
			List<Integer> companyIds = companyService.getAllCompanyIds();
			logger.info("init companyIds : " + companyIds);
			CompanyDBCacheManager.instance.init(companyIds);

			try {
				hotSaleMerchantProductService.tryReload();
			} catch (Exception e) {
				logger.error("init hotSaleMerchantProductService error  "+e.getMessage(),e);
			}

			event.getApplicationContext().publishEvent(new DBManagerInitedEvent(event.getApplicationContext()));
		}
	
	}

}
