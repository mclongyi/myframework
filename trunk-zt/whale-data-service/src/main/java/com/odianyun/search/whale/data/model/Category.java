package com.odianyun.search.whale.data.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Category {
	
	private static int MAX_LIST_SORT=9999;

	private long id;
	
	private String name;
	
	private long parentId;

	private String fullIdPath;

	private long firstCategoryId;

	private int listSort=MAX_LIST_SORT;

	private List<Long> fullIdPathList;

	public static final Map<String, String> resultMap = new HashMap<String, String>();
	
	static{
		resultMap.put("id", "id");
		resultMap.put("name", "name");
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", name=" + name + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	public static Map<String, String> getResultmap() {
		return resultMap;
	}
	
	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public String getFullIdPath() {
		return fullIdPath;
	}

	public void setFullIdPath(String fullIdPath) {
		this.fullIdPath = fullIdPath;
	}

	public long getFirstCategoryId() {
		return firstCategoryId;
	}

	public void setFirstCategoryId(long firstCategoryId) {
		this.firstCategoryId = firstCategoryId;
	}

	public List<Long> getFullIdPathList() {
		return fullIdPathList;
	}

	public void setFullIdPathList(List<Long> fullIdPathList) {
		this.fullIdPathList = fullIdPathList;
	}

	public int getListSort() {
		return listSort;
	}

	public void setListSort(int listSort) {
		this.listSort = listSort;
	}

}
