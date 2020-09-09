package com.odianyun.search.whale.common.cache.ocache;

import com.google.gson.Gson;
import org.apache.commons.codec.digest.DigestUtils;

import com.odianyun.cache.CacheProxy;

/**
 * MD5SearchCache 是一个可以直接使用的cache proxy
 * 如果想更加定制化,子类一般可以重写filterKey(),filter(),doGenerate(),caclCacheTime()方法
 */
public class MD5OCache<K,V> extends BaseCache<K,V>{

	static Gson gson = new Gson();

	public MD5OCache(){
		super();
	}
	
	public MD5OCache(CacheProxy cacheProxy){
		super(cacheProxy);
	}
	
	@Override
	protected final String generateKey(K k) {
		return DigestUtils.md5Hex(doGenerate(k));
	}

	protected String doGenerate(K k) {
		return gson.toJson(k);
	}

    

}
