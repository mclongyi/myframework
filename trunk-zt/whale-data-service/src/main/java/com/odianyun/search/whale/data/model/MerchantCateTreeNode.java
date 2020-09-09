package com.odianyun.search.whale.data.model;

import java.util.HashMap;
import java.util.Map;

public class MerchantCateTreeNode {

	private long id;
	private long parent_id;
	private long merchant_cat_tree_id;
	private long category_id;
	private String name;

	public static final Map<String, String> resultMap = new HashMap<String, String>();

	static{
		resultMap.put("id","id");
		resultMap.put("parent_id","parent_id");
		resultMap.put("category_id","category_id");
		resultMap.put("merchant_cat_tree_id","merchant_cat_tree_id");
		resultMap.put("name","name");
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getParent_id() {
		return parent_id;
	}
	public void setParent_id(long parent_id) {
		this.parent_id = parent_id;
	}
	public long getMerchant_cat_tree_id() {
		return merchant_cat_tree_id;
	}
	public void setMerchant_cat_tree_id(long merchant_cat_tree_id) {
		this.merchant_cat_tree_id = merchant_cat_tree_id;
	}
	public long getCategory_id() {
		return category_id;
	}
	public void setCategory_id(long category_id) {
		this.category_id = category_id;
	}
	public static Map<String, String> getResultmap() {
		return resultMap;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
	
