package com.odianyun.search.whale.data.model.geo;

import java.util.HashMap;
import java.util.Map;

/**
 * O2O领域下，店铺商家的配送范围
 * @author yuqian
 *
 */
public class DeliveryArea {

	private long merchant_id;// 商家id
	private int type;// 1:标准覆盖；2：自定义覆盖
	private long nation_id=0; // 国家id
	private long province_id=0; // 省份id
	private long city_id=0; // 城市id
	private long region_id=0;// 区域id
	private long areaCode;//区域code
	private String detail_addr;// 详细地址
	private String poi_addr;// 自定覆盖时 存的多个点阵的值; 一个点阵多个poi, 每个poi 都有顺序
	private int priority=0; //优先级
	private long companyId;

	public static final Map<String, String> resultMap = new HashMap<String, String>();
	
	static{
		resultMap.put("merchant_id","merchant_id");
        resultMap.put("type","type");
        resultMap.put("nation_id","nation_id" );
        resultMap.put("province_id","province_id" );
        resultMap.put("city_id","city_id" );
        resultMap.put("region_id","region_id" );
        resultMap.put("detail_addr","detail_addr");
        resultMap.put("poi_addr","poi_addr");
        resultMap.put("priority","priority" );
        resultMap.put("companyId","company_id");
	}
	public long getMerchant_id() {
		return merchant_id;
	}
	public void setMerchant_id(long merchant_id) {
		this.merchant_id = merchant_id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public long getNation_id() {
		return nation_id;
	}
	public void setNation_id(long nation_id) {
		this.nation_id = nation_id;
	}
	public long getProvince_id() {
		return province_id;
	}
	public void setProvince_id(long province_id) {
		this.province_id = province_id;
	}
	public long getCity_id() {
		return city_id;
	}
	public void setCity_id(long city_id) {
		this.city_id = city_id;
	}
	public long getRegion_id() {
		return region_id;
	}
	public void setRegion_id(long region_id) {
		this.region_id = region_id;
	}
	public String getDetail_addr() {
		return detail_addr;
	}
	public void setDetail_addr(String detail_addr) {
		this.detail_addr = detail_addr;
	}
	public String getPoi_addr() {
		return poi_addr;
	}
	public void setPoi_addr(String poi_addr) {
		this.poi_addr = poi_addr;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}
	public long getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(long areaCode) {
		this.areaCode = areaCode;
	}
	public static Map<String, String> getResultmap() {
		return resultMap;
	}

}
