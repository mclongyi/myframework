package com.odianyun.search.whale.common.cache;

/**
 * Created by zengfenghua on 16/10/22.
 */
public interface ICache<K,V> {

	V get(K k);

	void put(K k, V v);
	
	void put(K k, V v, int expireMins);

	void remove(K k);

	void remove(String k);
	
	boolean isOpenCache();
	
    void openCache();
    
	void closeCache();
	
    CacheStats getCacheStats();

}
