package com.odianyun.search.autobot.index.suggest;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.odianyun.search.whale.index.suggest.server.RestSuggestService;

public class ServiceTest {
	public static void main(String[] args) {
		System.setProperty("global.config.path","/Users/fishcus/JavaDev/data/env");

		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext-beans.xml");
		RestSuggestService service = 		(RestSuggestService) applicationContext.getBean("restSuggestService");
		
		
		System.out.println("-------------");
	}
}
