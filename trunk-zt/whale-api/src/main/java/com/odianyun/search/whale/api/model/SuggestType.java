package com.odianyun.search.whale.api.model;

public enum SuggestType {
	
	KEYWORD(0),
	AREA(1),
    POINT(2)
	;
	private Integer code;

	private SuggestType(Integer code){
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
