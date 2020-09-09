package com.odianyun.search.whale.data.model;

import java.util.HashMap;
import java.util.Map;

public class MerchantProductForSuggest {
	
	private long id;
	
	private long productId;
	
	private long merchantId;
	
	private String chineseName;
	
	private String englishName;

	private String subtitle;//副标题(商家自定义名称)

	private long companyId=0;

	private Long categoryId;

	private Long brandId;

	private String categoryName;

	private String brandName;

	private String brandChineseName;

	private String brandEnglishName;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(long merchantId) {
		this.merchantId = merchantId;
	}

	public String getChineseName() {
		return chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getBrandChineseName() {
		return brandChineseName;
	}

	public void setBrandChineseName(String brandChineseName) {
		this.brandChineseName = brandChineseName;
	}

	public String getBrandEnglishName() {
		return brandEnglishName;
	}

	public void setBrandEnglishName(String brandEnglishName) {
		this.brandEnglishName = brandEnglishName;
	}
}
