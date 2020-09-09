package com.odianyun.search.whale.data.model;

/**
 * Created by Fu Yifan on 2017/8/19.
 */
public class MerchantCategorySaleArea {
    private Long merchantId;
    private Long categoryId;
    private Long saleAreaId;

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getSaleAreaId() {
        return saleAreaId;
    }

    public void setSaleAreaId(Long saleAreaId) {
        this.saleAreaId = saleAreaId;
    }
}
