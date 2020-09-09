package com.odianyun.search.whale.data.model;

/**
 * Created by Janz on 2017/3/20.
 */
public class MerchantProductRelation {
    //父商家的商品id
    private Long refId;
    //门店商家的商品id
    private Long mpId;

    public Long getRefId() {
        return refId;
    }

    public void setRefId(Long refId) {
        this.refId = refId;
    }

    public Long getMpId() {
        return mpId;
    }

    public void setMpId(Long mpId) {
        this.mpId = mpId;
    }
}
