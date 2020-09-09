package com.odianyun.search.whale.index.api.common;

public enum UpdateType {
	
	merchant_product_id, //商品id
	
	product_id,   //产品Id

	category_tree_node_id, //类目treeNodeID
	
	brand_id,    //品牌Id
	
	merchant_series_id,    //系列品Id

	FULL_INDEX,    //全量索引

	GEO_MERCHANT_ID,  // 更新geo索引  merchantId
	product_series_id,    //系列品Id(虚码)

	points_mall_product_id, //积分商城商品

}
