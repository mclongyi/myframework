package com.odianyun.search.whale.common.cache.ocache;

import com.odianyun.cache.CacheProxy;
import com.odianyun.cache.MemcacheExtend;
import com.odianyun.cc.client.spring.OccPropertiesLoaderUtils;
import org.apache.log4j.Logger;

import java.io.File;

public class CacheBuilder {

	private static Logger log = Logger.getLogger(CacheBuilder.class);
	private static final String memcache_id = "search";
	private static final String poolId = "search";
	private static final String config_file = "memcache.xml";

	public static synchronized CacheProxy buildCache(){
		CacheProxy cacheProxy=null;
        try{
			cacheProxy=buildCache(memcache_id,poolId,"/search/common/"+config_file);
		}catch(Exception e){
            log.error(e.getMessage(),e);
		}
		return cacheProxy;

	}

	public static synchronized CacheProxy buildCache(String memcacheId,String poolId,String filePath){
		CacheProxy cacheProxy=null;
		try{
			File configFile = OccPropertiesLoaderUtils.getFile(poolId,filePath);
			filePath = "file:" + configFile.getAbsolutePath();
		}catch(Exception e){
			log.error(e.getMessage(),e);
		}
		try{
			MemcacheExtend memcacheExtend=MemcacheExtend.getInstance(filePath);
			cacheProxy= memcacheExtend.getCacheProxy(memcacheId);
			log.info("CacheBuilder buildCache() successful");
		}catch (Throwable t){
			log.error(t.getMessage(),t);
		}
		return cacheProxy;
	}



	public static void main(String[] args){
		System.setProperty("global.config.path", "/data/ds_trunk_env");
		OccPropertiesLoaderUtils.getProperties("osoa");
		CacheProxy cacheProxy=buildCache("product","basics-price-service","basics-price/basics-price-business/basics-price-business-memcache.xml");
	//	CacheProxy cacheProxy=buildCache("search","search","search/common/memcache.xml");
		cacheProxy.put("test_000","99999");
		System.out.println(cacheProxy.get("test_000"));
	}

}
