package com.odianyun.search.whale.data.common;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;

@SuppressWarnings("serial")
public class DBManagerInitedEvent extends ApplicationContextEvent{

	public DBManagerInitedEvent(ApplicationContext source) {
		super(source);
	}

}
