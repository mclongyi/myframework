package com.odianyun.search.whale.data.model.geo;

import java.util.HashMap;
import java.util.Map;

public class POI {
	
	private Long refId;
	
	private Integer refType;
	
	private Double longitude; //经度
 
	private Double latitude; //纬度 

	private Integer poiType;

	private String addr;

	private Long provinceId;
	private Long cityId;
	private Long regionId;


	public Long getRefId() {
		return refId;
	}

	public void setRefId(Long refId) {
		this.refId = refId;
	}

	public Integer getRefType() {
		return refType;
	}

	public void setRefType(Integer refType) {
		this.refType = refType;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Integer getPoiType() {
		return poiType;
	}

	public void setPoiType(Integer poiType) {
		this.poiType = poiType;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}
}
