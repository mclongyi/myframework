package com.odianyun.search.whale.common.cache.ocache;

import com.odianyun.cache.CacheProxy;
import com.odianyun.search.whale.common.cache.CacheStats;
import com.odianyun.search.whale.common.cache.ICache;
import org.apache.log4j.Logger;

/**
 * Created by zengfenghua on 16/10/22.
 * BaseCache是一个可以直接使用的cache proxy
 * 如果想更加定制化,子类一般可以重写filterKey(),filter(),generateKey(),caclCacheTime()方法
 */
public class BaseCache<K,V> implements ICache<K,V> {

    private static Logger logger = Logger.getLogger(BaseCache.class);

    private static final int defaultExpireMins=10;

    private CacheProxy cacheProxy=null;

    private CacheStats cacheStats=new CacheStats();

    private boolean openCache=true;

    public BaseCache(){
        try{
            cacheProxy= CacheBuilder.buildCache();
        }catch(Throwable e){
            logger.error(e.getMessage(),e);
        }
    }

    public BaseCache(CacheProxy cacheProxy){
        this.cacheProxy=cacheProxy;
    }

    private boolean canGet(K k){
        if(cacheProxy!=null && isOpenCache() && !filterKey(k)){
            return true;
        }
        return false;
    }


    private boolean canPut(K k,V v){
        if(canGet(k) && !filter(k,v)){
            return true;
        }
        return false;
    }

    @Override
    public V get(K k){
        if(!canGet(k)){
            return null;
        }
        cacheStats.incAccess(1);
        V v=null;
        try{
            v= (V) cacheProxy.get(generateKey(k));
        }catch(Throwable e){
            logger.error(e.getMessage(),e);
            cacheStats.incAccessTimeout(1);
        }
        if(v!=null){
            cacheStats.incHit(1);
        }
        return v;
    }

    @Override
    public void put(K k, V v){
        put(k,v,caclCacheTime(k,v));
    }

    @Override
    public void put(K k, V v, int expireMins){
        if(!canPut(k,v)){
            return;
        }
        try{
            cacheProxy.put(generateKey(k),v,expireMins);
        }catch(Throwable e){
            logger.error(e.getMessage(),e);
            cacheStats.incAccessTimeout(1);
        }
    }

    @Override
    public void remove(K k){
        if(!canGet(k)){
            return;
        }
        remove(generateKey(k));

    }

    @Override
    public void remove(String k) {
        try{
            cacheProxy.remove(k);
        }catch(Throwable e){
            logger.error(e.getMessage(),e);
            cacheStats.incAccessTimeout(1);
        }
    }

    /**
     * 是否过滤这个key,不满足缓存的条件
     * @param k
     * @return
     */
    protected boolean filterKey(K k){
        return false;
    }


    /**
     * 是否过滤这个K和V,不满足缓存的条件
     * @param v
     * @return
     */
    protected boolean filter(K k,V v){
        if(filterKey(k)){
            return true;
        }
        return false;
    }

    /**
     * 生成真正的缓存的key
     * @param k
     * @return
     */
    protected String generateKey(K k) {
        return k.toString();
    }
    
    

    /**
     * 计算缓存的时间,单位为分钟
     * @param k
     * @param v
     * @return
     */
    protected int caclCacheTime(K k,V v){
        return defaultExpireMins;
    }

    @Override
    public boolean isOpenCache() {
        return openCache;
    }

    @Override
    public void openCache() {
        openCache=true;
    }

    @Override
    public void closeCache() {
        openCache=false;
    }

    @Override
    public CacheStats getCacheStats() {
        return cacheStats;
    }

}
