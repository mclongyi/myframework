package com.odianyun.search.whale.api.model.resp;

import com.odianyun.search.whale.common.cache.CacheInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fishcus on 16/11/13.
 */
@Data
@NoArgsConstructor
public class PromotionTypeSearchResponse implements Serializable{

    private  List<Integer> promotionTypeList = new ArrayList<>();

    //处理花费时间，毫秒
    private long costTime;

    //缓存的key值
    private CacheInfo cacheInfo;

    public List<Integer> getPromotionTypeList() {
        return promotionTypeList;
    }

    public void setPromotionTypeList(List<Integer> promotionTypeList) {
        this.promotionTypeList = promotionTypeList;
    }

    public long getCostTime() {
        return costTime;
    }

    public void setCostTime(long costTime) {
        this.costTime = costTime;
    }

    public CacheInfo getCacheInfo() {
        return cacheInfo;
    }

    public void setCacheInfo(CacheInfo cacheInfo) {
        this.cacheInfo = cacheInfo;
    }
}
