package com.odianyun.search.whale.api.model.resp;

import com.odianyun.search.whale.api.model.MerchantProduct;
import com.odianyun.search.whale.common.cache.CacheInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.meta.TypeQualifierDefault;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fishcus on 16/11/13.
 */
@Data
@NoArgsConstructor
public class PromotionSearchResponse implements Serializable {

    //merchantProduct结果集
    private List<MerchantProduct> merchantProductResult = new ArrayList<MerchantProduct>();

    private long totalHits;

    //处理花费时间，毫秒
    private long costTime;

    //缓存的key值
    private CacheInfo cacheInfo;

    public List<MerchantProduct> getMerchantProductResult() {
        return merchantProductResult;
    }

    public void setMerchantProductResult(List<MerchantProduct> merchantProductResult) {
        this.merchantProductResult = merchantProductResult;
    }

    public long getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(long totalHits) {
        this.totalHits = totalHits;
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
