package com.odianyun.search.whale.api;

import com.odianyun.cc.client.spring.OccPropertiesLoaderUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class AbstractTest{
	
	static{
		System.setProperty("global.config.path", "D:\\workspace\\prop\\env\\proprities_center\\lyf-branch");
		OccPropertiesLoaderUtils.getProperties("osoa");
	}

}
