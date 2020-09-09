package com.odianyun.search.whale.data.model;

/**
 * Created by Fu Yifan on 2017/8/19.
 */
public class SupplierMerchantProductRel {
    private Long merchantProductId;
    private Long supplierId;

    public Long getMerchantProductId() {
        return merchantProductId;
    }

    public void setMerchantProductId(Long merchantProductId) {
        this.merchantProductId = merchantProductId;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }
}
