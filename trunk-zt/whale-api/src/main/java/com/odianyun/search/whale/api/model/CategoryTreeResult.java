package com.odianyun.search.whale.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CategoryTreeResult implements java.io.Serializable{
	
	private long id;
	
	private String name;
	
	private List<CategoryTreeResult> children;
	
	private long count;

	public CategoryTreeResult(long id,String name,long count){
		this.id = id;
		this.name = name;
		this.count =count;
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

	public List<CategoryTreeResult> getChildren() {
		return children;
	}

	public void setChildren(List<CategoryTreeResult> children) {
		this.children = children;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}
	
	public void addCount(long count){
		this.count += count;
	}

	public void addChild(CategoryTreeResult childTreeResult) {
		if (this.children == null) {
			this.children = new ArrayList<CategoryTreeResult>();
			this.children.add(childTreeResult);
		} else {
			int i = 0;
			for (; i < children.size(); i++) {
				CategoryTreeResult child = children.get(i);
				if (child.getCount() < childTreeResult.getCount()) {
					break;
				}
			}
			if (i != children.size()) {
				this.children.add(i, childTreeResult);
			} else {
				this.children.add(childTreeResult);
			}
		}
	}
	@Override
	public String toString() {
		return "CategoryTreeResult [id=" + id + ", name=" + name
				+ ", children=" + children + ", count=" + count + "]";
	}
	
}
