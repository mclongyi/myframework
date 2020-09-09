package com.odianyun.search.whale.data.model;

/**
 * Created by fishcus on 16/11/24.
 */
public enum HotSaleType {
    //公司热卖
    COMPANY_HOT(0),
    //门店热卖
    MERCHANT_HOT(1),
    ;

    private Integer code;

    HotSaleType(Integer code){
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
