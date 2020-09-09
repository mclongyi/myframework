package com.odianyun.search.whale.index.suggest.common;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.odianyun.search.whale.data.common.util.SpringContextUtil;



public class ProcessorApplication {
	static Logger log = Logger.getLogger(ProcessorApplication.class);
    public static boolean enable_debug = false;
    static ApplicationContext applicationContext = null;

    public static synchronized void loadContext() {
    	applicationContext=SpringContextUtil.getApplicationContext();
    	log.info("SpringContextUtil.getApplicationContext()=="+applicationContext);
        if (applicationContext == null) {
            String configPath = System.getProperty("global.config.path");
            if (configPath == null) {
                System.setProperty("global.config.path", "/data/env");
            }
            applicationContext = new ClassPathXmlApplicationContext(
                    "processorApplicationContext.xml");
        }
        
    }

    public static void setGlobalConfigPath(String configPath) {
        System.setProperty("global.config.path", configPath);
        System.setProperty("hadoop.home.dir", "...");
    }

    public static Object getBean(String name) {
        if (applicationContext == null) {
            loadContext();
        }

        return applicationContext.getBean(name);
    }

}
