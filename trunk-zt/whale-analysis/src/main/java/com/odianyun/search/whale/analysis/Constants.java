package com.odianyun.search.whale.analysis;

public final class Constants {

	public static final String CONFIG_PATH = "global.config.path";
	public static final String DEFAULT_CONFIG = "/data/env/snapshot";

	public static final String IK_HOME = "path.home";
	public static final String SEARCH_RESOURCE = "/search";

	public static final String SYNONYMY_DICT = "/config/synonymy.dic";
	public static final String EXTEND_DICT = "/config/extend.dic";

	public static String getGlobalPath() {
    	return System.getProperty(CONFIG_PATH, DEFAULT_CONFIG)+"/snapshot";
	}

	public static String getSynonymyPath() {
		String global = getGlobalPath();
		return global + SEARCH_RESOURCE + SYNONYMY_DICT;
	}

	public static String getExtendPath() {
		String global = getGlobalPath();
		return global + SEARCH_RESOURCE + EXTEND_DICT;
	}

	private Constants() {
		//do nothing else
	}
}
