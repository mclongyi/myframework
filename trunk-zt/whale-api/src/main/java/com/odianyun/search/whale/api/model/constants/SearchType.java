package com.odianyun.search.whale.api.model.constants;

/**
 * Created by fishcus on 16/11/13.
 */
public enum SearchType {
    PROMOTION_SEARCH(1),
    PROMOTION_TYPE_SEARCH(2),

    ;

    private int code;

    private SearchType(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
