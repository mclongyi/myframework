package com.odianyun.search.whale.api.model;

public enum ManagementType {
	//审核通过
	VERIFIED(2),
	//上架
	ON_SHELF(1),
	//下架
	OFF_SHELF(0),
	;
	private Integer code;

	private ManagementType(Integer code){
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
