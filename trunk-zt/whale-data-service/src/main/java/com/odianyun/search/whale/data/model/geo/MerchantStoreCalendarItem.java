package com.odianyun.search.whale.data.model.geo;

import java.util.Date;

/**
 * Created by fishcus on 16/12/22.
 */
public class MerchantStoreCalendarItem {

    private Long merchantStoreCalendarId;

    private String beginDate;

    private String endDate;

    public Long getMerchantStoreCalendarId() {
        return merchantStoreCalendarId;
    }

    public void setMerchantStoreCalendarId(Long merchantStoreCalendarId) {
        this.merchantStoreCalendarId = merchantStoreCalendarId;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
