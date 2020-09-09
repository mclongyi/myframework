package com.odianyun.search.whale.data.model;

public class ProductCategoryRelation {

	private long productId;
	private int type;
	private long naviCategoryId;
	private long backendCategoryId;
	
	public long getProductId() {
		return this.productId;
	}
	
	public void setProductId(long productId) {
		this.productId = productId;
	}
	
	public int getType() {
		return this.type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public long getNaviCategoryId() {
		return this.naviCategoryId;
	}
	
	public void setNaviCategoryId(long naviCategoryId) {
		this.naviCategoryId = naviCategoryId;
	}
	
	public long getBackendCategoryId() {
		return this.backendCategoryId;
	}
	
	public void setBackendCategoryId(long backendCategoryId) {
		this.backendCategoryId = backendCategoryId;
	}
	
}
