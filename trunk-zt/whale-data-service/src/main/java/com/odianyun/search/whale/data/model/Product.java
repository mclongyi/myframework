package com.odianyun.search.whale.data.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Product {
	
	private long id;
	
//	private long category_tree_node_id;
	private long category_id;
	
	private Long brandId;

	//原始ean码
	private String ean_no;

	private String code;

	//计量单位:5kg/箱
	private String calculation_unit;

	//产品规格
	private String standard;

	public static final Map<String, String> resultMap = new HashMap<String, String>();

	static{
		resultMap.put("id", "id");
		resultMap.put("brandId", "brand_id");
		resultMap.put("category_tree_node_id", "category_tree_node_id");
		resultMap.put("ean_no", "ean_no");
		resultMap.put("code", "code");
		resultMap.put("calculation_unit", "calculation_unit");
		resultMap.put("standard", "standard");
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	/*public long getCategory_tree_node_id() {
		return category_tree_node_id;
	}

	public void setCategory_tree_node_id(long category_tree_node_id) {
		this.category_tree_node_id = category_tree_node_id;
	}*/

	public void setEan_no(String ean_no) {
		this.ean_no = ean_no;
	}

	public long getCategory_id() {
		return category_id;
	}

	public void setCategory_id(long category_id) {
		this.category_id = category_id;
	}

	public String getEan_no() {
		return ean_no;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCalculation_unit() {
		return calculation_unit;
	}

	public void setCalculation_unit(String calculation_unit) {
		this.calculation_unit = calculation_unit;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", category_tree_node_id="
				+ category_id + ", brandId=" + brandId + ", ean_no="
				+ ean_no +", code=" + code + ", calculation_unit="
				+ calculation_unit + ", standard=" + standard + "]";
	}
	public static Map<String, String> getResultmap() {
		return resultMap;
	}

}
