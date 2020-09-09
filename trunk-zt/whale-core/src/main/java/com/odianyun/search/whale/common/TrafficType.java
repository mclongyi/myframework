package com.odianyun.search.whale.common;

/**
 * Created by fishcus on 16/12/29.
 */
public enum TrafficType {
    //步行
    WORKING(0),
    ///私家车
    DRIVING(1),
    //公共交通
    TRANSIT(2),
    ;
    private Integer code;
    TrafficType(Integer code){
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
