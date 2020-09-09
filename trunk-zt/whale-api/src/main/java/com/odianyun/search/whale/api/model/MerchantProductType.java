package com.odianyun.search.whale.api.model;

public enum MerchantProductType {
	// 普通商品
	NORMAL(1),
	// 生鲜
	FRESH(2),
	// 卡券
	CARD(3),
	// 增值服务
	VALUE_ADDED_SERVICE(4),
	// 其他
	OTHER(5),;
	private Integer code;

	private MerchantProductType(Integer code) {
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}
}
