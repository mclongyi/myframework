package com.odianyun.search.whale.data.model;

import java.util.HashMap;
import java.util.Map;

public class MerchantProdMerchantCateTreeNode {
	
	private Long merchantCateTreeNodeId;
	
	private Long merchantMroductId;
	
	public static final Map<String, String> resultMap = new HashMap<String, String>();
	
	static{
		resultMap.put("merchantCateTreeNodeId","merchant_cate_tree_node_id");
		resultMap.put("merchantMroductId","merchant_product_id");
	}

	public Long getMerchantCateTreeNodeId() {
		return merchantCateTreeNodeId;
	}

	public void setMerchantCateTreeNodeId(Long merchantCateTreeNodeId) {
		this.merchantCateTreeNodeId = merchantCateTreeNodeId;
	}

	public Long getMerchantMroductId() {
		return merchantMroductId;
	}

	public void setMerchantMroductId(Long merchantMroductId) {
		this.merchantMroductId = merchantMroductId;
	}
	public static Map<String, String> getResultmap() {
		return resultMap;
	}
}
