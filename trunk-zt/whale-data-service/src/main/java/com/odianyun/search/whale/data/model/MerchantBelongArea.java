package com.odianyun.search.whale.data.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 商家所属区域，非覆盖区域
 * @author yuqian
 *
 */
public class MerchantBelongArea {

	private Long id;
	private Long merchant_id;
	private Long nation_id;
	private Long province_id;
	private Long city_id;
	private Long region_id;
	private Long area_code;
	
	public static final Map<String, String> resultMap = new HashMap<String, String>();
	
	static{
		resultMap.put("merchant_id","merchant_id" );
        resultMap.put("nation_id","nation_id" );
        resultMap.put("province_id","province_id" );
        resultMap.put("city_id","city_id" );
        resultMap.put("region_id","region_id" );
        resultMap.put("area_code","area_code" );
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMerchant_id() {
		return merchant_id;
	}

	public void setMerchant_id(Long merchant_id) {
		this.merchant_id = merchant_id;
	}

	public Long getNation_id() {
		return nation_id;
	}

	public void setNation_id(Long nation_id) {
		this.nation_id = nation_id;
	}

	public Long getProvince_id() {
		return province_id;
	}

	public void setProvince_id(Long province_id) {
		this.province_id = province_id;
	}

	public Long getCity_id() {
		return city_id;
	}

	public void setCity_id(Long city_id) {
		this.city_id = city_id;
	}

	public Long getRegion_id() {
		return region_id;
	}

	public void setRegion_id(Long region_id) {
		this.region_id = region_id;
	}

	public Long getArea_code() {
		return area_code;
	}

	public void setArea_code(Long area_code) {
		this.area_code = area_code;
	}
	public static Map<String, String> getResultmap() {
		return resultMap;
	}
}
