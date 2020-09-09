package com.odianyun.search.whale.store.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;


/**
 * 该类用来获取配置参数和环境变量
 * 
 * @author zengfenghua
 *
 */
public class DataStoreConfig {
	private static Logger log = Logger.getLogger(DataStoreConfig.class);
	private static String env = "";
	/**
	 * 获取env
	 */
	static {
		String envPath = "global.config.path";
		String cfgPath = System.getProperty(envPath);
		if (cfgPath == null) {
			cfgPath = "/data/env/";
			System.setProperty(envPath, cfgPath);
		}
		
		FileInputStream fis = null;
		try {
			File envFile = new File(cfgPath, "env.ini");
			if (envFile.exists()) {
				fis = new FileInputStream(envFile);
				Properties prop = new Properties();
				prop.load(fis);
				env = prop.getProperty("env");
			} else {
				log.error("File doesn't exist, " + envFile.getAbsolutePath());
			}
		} catch(IOException e) {
			log.error("", e);
		} finally {
			if (fis != null) {
				try{
					fis.close();
				} catch(IOException e1){
					log.error("", e1);
				}
			}
		}
		
		log.info("env: " + env);
	}
	
	public static String getZk_quorums(){
		return ConfigCenterUtil.get("zk_quorums");
	}
	
	public static String getFs_name(){
		return ConfigCenterUtil.get("ha_fs_name", "hdfs://Search:8020");
	}
	
	public static String getYarn_host(){
		return ConfigCenterUtil.get("yarn_host");
	}
	
	public static String getEnv(){
		return env==null?env:env.trim();
	}
	
	public static void main(String[] args){
		System.out.println(env);
	}
	
}
