package com.odianyun.search.whale.server;

import com.odianyun.cc.client.spring.OccPropertiesLoaderUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class AbstractTest{
	
	protected static ApplicationContext context;
	
	static{
		System.setProperty("global.config.path", "/data/env/jkl_trunk");
		//context=new ClassPathXmlApplicationContext("applicationContext-beans.xml");
	}

}
