package com.odianyun.search.whale.index;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class AbstractTest{
	
	protected static ApplicationContext context;
	
	static{
		System.setProperty("env","test");
		System.setProperty("global.config.path", "D:\\workspace\\prop\\env\\proprities_center\\lyf-branch");
		context=new ClassPathXmlApplicationContext("applicationContext-beans.xml");
	}

}
