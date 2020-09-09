package com.odianyun.search.whale.store.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.odianyun.cc.client.spring.OccPropertiesLoaderUtils;

/**
 * 
 * @author zengfenghua
 *
 */
public class ConfigCenterUtil {
	static Logger logger = Logger.getLogger(ConfigCenterUtil.class);

	private static String[] propNames = new String[] {
			"hadoop.properties"};
//	static String env=System.getProperty("global.config.path","/data/env");

	private static Properties properties = new Properties();
	static {
		String env = System.getProperty("global.config.path");
		if (env == null) {
			System.setProperty("global.config.path", "/data/env");
			env = System.getProperty("global.config.path");
		}
		String poolId = "whale-data-service";
		for (int i = 0; i < propNames.length; i++) {
			Properties prop=null;
			try {
				prop = OccPropertiesLoaderUtils.getProperties("search","/search/store/"+propNames[i]);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new RuntimeException(e);
			}
			if (prop != null) {
				properties.putAll(prop);
			}
		}
	}
	
	public static String get(String name) {
		if (name == null){
			return null;
		}
		return properties.getProperty(name);
	}

	public static String get(String name, String def) {
		String val = get(name);
		return val != null ? val : def;
	}

	public static int getInt(String name) {
		String val = get(name);
		return val == null ? 0 : Integer.parseInt(val);
	}

	public static int getInt(String name, int def) {
		String val = get(name);
		return val != null ? Integer.parseInt(val) : def;
	}
   
	public static long getLong(String name) {
		String val = get(name);
		return val == null ? 0L : Long.parseLong(val);
	}
	
	public static long getLong(String name, long def) {
		String val = get(name);
		return val != null ? Long.parseLong(val) : def;
	}
	
	public static boolean getBool(String name) {
		String val = get(name);
		return val == null ? false : Boolean.parseBoolean(val);
	}

	public static boolean getBool(String name, boolean def) {
		String val = get(name);
		return val != null ? Boolean.parseBoolean(val) : def;
	}

	public static float getFloat(String name) {
		String val = get(name);
		return val == null ? 0 : Float.parseFloat(val) ;
	}

	public static float getFloat(String name, float def) {
		String val = get(name);
		return val != null ? Float.parseFloat(val) : def;
	}

	public static double getDouble(String name) {
		String val = get(name);
		return val == null ? 0 : Double.parseDouble(val);
	}

	public static double getDouble(String name, double def) {
		String val = get(name);
		return val != null ? Double.parseDouble(val) : def;
	}

}
