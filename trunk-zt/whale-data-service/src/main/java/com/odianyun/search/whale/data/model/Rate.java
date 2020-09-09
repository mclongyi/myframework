package com.odianyun.search.whale.data.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by juzhongzheng on 2016-08-19.
 */
public class Rate {
    //商品id
    private Long mpId;
    //评分（均值）
    private Double rate;
    //好评率
    private Integer positiveRate;
    //评论数量
    private Integer ratingCount;
    //好评数量
    private Integer positiveCount;

    public static final Map<String, String> resultMap = new HashMap<String, String>();

    static{
        resultMap.put("mpId","mp_id");
        resultMap.put("rate","rate");
        resultMap.put("positiveRate","positive_rate");
        resultMap.put("ratingCount","rating_count");
        resultMap.put("positiveCount", "positive_count");
    }

    public Long getMpId() {
        return mpId;
    }

    public void setMpId(Long mpId) {
        this.mpId = mpId;
    }

    public static Map<String, String> getResultMap() {
        return resultMap;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Integer getPositiveRate() {
        return positiveRate;
    }

    public void setPositiveRate(Integer positiveRate) {
        this.positiveRate = positiveRate;
    }

    public Integer getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Integer ratingCount) {
        this.ratingCount = ratingCount;
    }

    public Integer getPositiveCount() {
        return positiveCount;
    }

    public void setPositiveCount(Integer positiveCount) {
        this.positiveCount = positiveCount;
    }
}
