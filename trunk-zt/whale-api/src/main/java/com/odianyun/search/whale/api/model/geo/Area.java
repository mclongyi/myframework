package com.odianyun.search.whale.api.model.geo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 地图返回当前位置的区域名称，程序里需要做一个转换，转为程序里定义的区域
 * 
 * @author zengfenghua
 *
 */
@Data
@NoArgsConstructor
public class Area implements java.io.Serializable{
	//国家名称：中国
	private String countryName;
	//省份名称:上海/湖南省
	private String provinceName;
	//城市名称:上海市/长沙市
	private String cityName;
	//区县名称:浦东新区/宁乡县
	private String areaName;
	
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	
	
	

}
