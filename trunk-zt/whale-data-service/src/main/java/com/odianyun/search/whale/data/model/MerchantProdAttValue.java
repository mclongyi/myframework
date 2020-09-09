package com.odianyun.search.whale.data.model;

/**
 * Created by Fu Yifan on 2017/9/14.
 */
public class MerchantProdAttValue {
    private Long merchantProductId;
    private Long attValueId;
    private String attValueCustom;

    public Long getAttValueId() {
        return attValueId;
    }

    public void setAttValueId(Long attValueId) {
        this.attValueId = attValueId;
    }

    public String getAttValueCustom() {
        return attValueCustom;
    }

    public void setAttValueCustom(String attValueCustom) {
        this.attValueCustom = attValueCustom;
    }

    public Long getMerchantProductId() {
        return merchantProductId;
    }

    public void setMerchantProductId(Long merchantProductId) {
        this.merchantProductId = merchantProductId;
    }
}
