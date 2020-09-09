package com.odianyun.search.whale.data.model.geo;

/**
 * Created by fishcus on 16/12/22.
 */
public enum MerchantStoreCalendarType {
    CUSTOM(1),
    ALL_DAY(2),
    RETIREE(3),
    ;

    private Integer code;

    MerchantStoreCalendarType(Integer code){
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

}
