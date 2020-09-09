package com.odianyun.search.whale.data.model;

import java.util.HashMap;
import java.util.Map;

public class CategoryTreeNodeRelation {
	// 已经改为leftCategoryId
	private long leftCategoryTreeNodeId;
	// 已经改为rightCategoryId
	private long rightCategoryTreeNodeId;
	
	public static final Map<String, String> resultMap = new HashMap<String, String>();
	
	static{
		resultMap.put("leftCategoryTreeNodeId", "left_tree_node_id");
		resultMap.put("rightCategoryTreeNodeId", "right_tree_node_id");
	}

	public long getLeftCategoryTreeNodeId() {
		return leftCategoryTreeNodeId;
	}

	public void setLeftCategoryTreeNodeId(long leftCategoryTreeNodeId) {
		this.leftCategoryTreeNodeId = leftCategoryTreeNodeId;
	}

	public long getRightCategoryTreeNodeId() {
		return rightCategoryTreeNodeId;
	}

	public void setRightCategoryTreeNodeId(long rightCategoryTreeNodeId) {
		this.rightCategoryTreeNodeId = rightCategoryTreeNodeId;
	}

	@Override
	public String toString() {
		return "CategoryTreeNodeRelation [leftCategoryTreeNodeId="
				+ leftCategoryTreeNodeId + ", rightCategoryTreeNodeId="
				+ rightCategoryTreeNodeId + "]";
	}
	public static Map<String, String> getResultmap() {
		return resultMap;
	}

}
