package com.odianyun.search.whale.index.api.common;

public class IndexConstants {
	
	public static final String indexName_pre="b2c_";
	
	public static final String index_alias="b2c_alias";
	
	public static final String index_type="mp";
	
	public static final String index_mapping_name="b2c_mapping.json";
	
	public static final int queue_size=20;
	
	public static final int thread_num=4;
		
	public static final int batch_num=200;

	public static final int pageSize=2000;
	
	public static final String CACHE_TOPIC = "search_cache_update";
	
	public static final String SEARCH_HISTORY_TOPIC = "search_history";
	
	public static final String OMQ_NAMESPACE = "saas";

	public static final int DEFAULT_COMPANY_ID = -1;
	
	public static final int HAS_STOCK = 1;
	
	public static final int HAS_PIC = 2;

	public static final String MAP_KEY = "6fc0b8f1d1095f96afcba6da65ebaeaf";

	public static final String MAP_URL = "http://restapi.amap.com/v3/geocode/geo?key="+MAP_KEY;

	public static final String MAP_WORKING_URL = "http://restapi.amap.com/v3/direction/walking?key="+MAP_KEY;

	public static final String MAP_DISTANCE_URL = "http://restapi.amap.com/v3/distance?key="+MAP_KEY;

	public static final int POINTS_SEARCH = 1;

	public static final int SEARCH = 0;

	public static final int LYF_COMPANY_ID = 30;

}
