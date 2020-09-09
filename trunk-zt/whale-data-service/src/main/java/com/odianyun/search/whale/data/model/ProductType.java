package com.odianyun.search.whale.data.model;

/**
 * Created by fishcus on 16/11/17.
 */
public enum ProductType {
    NORMAL(0),
    MAIN(1),
    SUB_CODE(2),
    VIRTUAL_CODE(3),
    COMBINE(4),
    ;
    private Integer code;
    ProductType(Integer code){
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
