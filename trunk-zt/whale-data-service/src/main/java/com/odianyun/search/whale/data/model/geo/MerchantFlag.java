package com.odianyun.search.whale.data.model.geo;

/**
 * Created by admin on 2016/11/23.
 */
public class MerchantFlag {
    private Long id;
    private Long merchantId;
    private Long merchantFlagId;
    private String merchantFlagCode;
    private String merchantFlagName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Long getMerchantFlagId() {
        return merchantFlagId;
    }

    public void setMerchantFlagId(Long merchantFlagId) {
        this.merchantFlagId = merchantFlagId;
    }

    public String getMerchantFlagCode() {
        return merchantFlagCode;
    }

    public void setMerchantFlagCode(String merchantFlagCode) {
        this.merchantFlagCode = merchantFlagCode;
    }

    public String getMerchantFlagName() {
        return merchantFlagName;
    }

    public void setMerchantFlagName(String merchantFlagName) {
        this.merchantFlagName = merchantFlagName;
    }
}
