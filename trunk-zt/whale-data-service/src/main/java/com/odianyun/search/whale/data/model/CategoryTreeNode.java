package com.odianyun.search.whale.data.model;

import java.util.HashMap;
import java.util.Map;

public class CategoryTreeNode {
	
	private static int MAX_LIST_SORT=9999;
	
	private Long id;
	
	private Long parent_id;
	
	private Long category_id;
	
	private int listSort=MAX_LIST_SORT;
	
	public static final Map<String, String> resultMap = new HashMap<String, String>();
	
	static{
		resultMap.put("id", "id");
		resultMap.put("parent_id", "parent_id");
		resultMap.put("category_id", "category_id");
		resultMap.put("listSort", "list_sort");
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParent_id() {
		return parent_id;
	}

	public void setParent_id(Long parent_id) {
		this.parent_id = parent_id;
	}

	public Long getCategory_id() {
		return category_id;
	}

	public void setCategory_id(Long category_id) {
		this.category_id = category_id;
	}

	public int getListSort() {
		return listSort;
	}

	public void setListSort(int listSort) {
		if(listSort<=0){
			this.listSort=MAX_LIST_SORT;
		}else{
			this.listSort = listSort;
		}
		
	}

	@Override
	public String toString() {
		return "CategoryTreeNode [id=" + id + ", parent_id=" + parent_id
				+ ", category_id=" + category_id + ", listSort=" + listSort
				+ "]";
	}
	public static Map<String, String> getResultmap() {
		return resultMap;
	}

}
