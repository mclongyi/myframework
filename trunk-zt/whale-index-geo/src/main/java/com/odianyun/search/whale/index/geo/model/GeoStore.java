package com.odianyun.search.whale.index.geo.model;


import com.odianyun.search.whale.data.model.geo.BusinessTime;

import java.util.List;

public class GeoStore {

	private Long merchantId;
	
	private Long shopId;
	
	private Long companyId;
	
	private String location;
	
	private Shape polygon;

	private String codeSearch;

	private String tag_words;

	private Integer merchantFlag;

	private Integer business_state;

	private List<BusinessTime> business_times;

	private Integer hasInSiteService;

	private Long parentId;

	private String areaCodes;
	
	private Integer merchantType;
	
	public Integer getMerchantType() {
		return merchantType;
	}


	public void setMerchantType(Integer merchantType) {
		this.merchantType = merchantType;
	}

	public Long getMerchantId() {
		return merchantId;
	}


	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}


	public Long getShopId() {
		return shopId;
	}


	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}


	public Long getCompanyId() {
		return companyId;
	}


	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}


	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}


	public Shape getPolygon() {
		return polygon;
	}


	public void setPolygon(Shape polygon) {
		this.polygon = polygon;
	}

	public String getCodeSearch() {
		return codeSearch;
	}

	public void setCodeSearch(String codeSearch) {
		this.codeSearch = codeSearch;
	}

	public String getTag_words() {
		return tag_words;
	}

	public void setTag_words(String tag_words) {
		this.tag_words = tag_words;
	}

	public void setMerchantFlag(Integer merchantFlag) {
		this.merchantFlag = merchantFlag;
	}

	public Integer getMerchantFlag() {
		return merchantFlag;
	}

	public List<BusinessTime> getBusiness_times() {
		return business_times;
	}

	public void setBusiness_times(List<BusinessTime> business_times) {
		this.business_times = business_times;
	}

	public Integer getBusiness_state() {
		return business_state;
	}

	public void setBusiness_state(Integer business_state) {
		this.business_state = business_state;
	}

	public Integer getHasInSiteService() {
		return hasInSiteService;
	}

	public void setHasInSiteService(Integer hasInSiteService) {
		this.hasInSiteService = hasInSiteService;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getAreaCodes() {
		return areaCodes;
	}

	public void setAreaCodes(String areaCodes) {
		this.areaCodes = areaCodes;
	}
}
