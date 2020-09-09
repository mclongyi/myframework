package com.odianyun.search.whale.data.model.geo;

/**
 * Created by fishcus on 16/12/22.
 */
public class MerchantStoreCalendar {

    private Long id;

    private Long merchantId;

    private Integer type;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
