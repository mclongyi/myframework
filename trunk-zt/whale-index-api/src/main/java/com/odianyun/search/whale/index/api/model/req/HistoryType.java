package com.odianyun.search.whale.index.api.model.req;

public enum HistoryType {
	
	SEARCH(0),
	MERCHANT(1),
    POINT(2)
	;
	private Integer code;

	private HistoryType(Integer code){
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
	
}
