package com.odianyun.search.whale.common.cache;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zengfenghua on 16/10/26.
 */
public class CacheInfo implements Serializable{
    public CacheInfo() {
    }

    /**
	 * 
	 */
	private static final long serialVersionUID = 815280756502988786L;

	private String cacheKey;

    private Date cacheTime;

    private long costTime;

    public String getCacheKey(){
        return cacheKey;
    }

    public void setCacheKey(String cacheKey){
        this.cacheKey=cacheKey;
    }

    public long getCostTime() {
        return costTime;
    }

    public void setCostTime(long costTime) {
        this.costTime = costTime;
    }

    public Date getCacheTime() {
        return cacheTime;
    }

    public void setCacheTime(Date cacheTime) {
        this.cacheTime = cacheTime;
    }

    @Override
    public String toString() {
        return "CacheInfo{" +
                "cacheKey='" + cacheKey + '\'' +
                ", cacheTime=" + cacheTime +
                ", costTime=" + costTime +
                '}';
    }
}
