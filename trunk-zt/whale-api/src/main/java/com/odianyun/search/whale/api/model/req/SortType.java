package com.odianyun.search.whale.api.model.req;

public enum SortType {
	
	create_time_asc,
    create_time_desc,

    //价格
    price_asc,
    price_desc,

    //积分价格
    point_price_asc,
    point_price_desc,

    //原价价格
    org_price_asc,
    org_price_desc,


    //销量
    volume4sale_asc,
    volume4sale_desc,
    
    //销量
    real_volume4sale_asc,
    real_volume4sale_desc,

    //商品佣金
    commodityCommission_asc,
    commodityCommission_desc,

    //评分
    rate_asc,
    rate_desc,

    //评论数
    rating_count_asc,
    rating_count_desc,

    //好评率
    positive_rate_asc,
    positive_rate_desc,

    //库存
    stock_asc,
    stock_desc,

    //首次上架時間
    first_shelf_time_asc,
    first_shelf_time_desc

}
