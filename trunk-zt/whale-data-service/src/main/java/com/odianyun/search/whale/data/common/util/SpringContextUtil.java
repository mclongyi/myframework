package com.odianyun.search.whale.data.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringContextUtil implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		if (applicationContext == null)
			applicationContext = context;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static Object getBean(String beanName) throws BeansException {
		return applicationContext.getBean(beanName);
	}

	public static Object getBean(String beanName, Class type)
			throws BeansException {
		return applicationContext.getBean(beanName, type);
	}

	public static boolean containsBean(String beanName) {
		return applicationContext.containsBean(beanName);
	}

	public static boolean isSingleton(String beanName) {
		return applicationContext.isSingleton(beanName);
	}
}