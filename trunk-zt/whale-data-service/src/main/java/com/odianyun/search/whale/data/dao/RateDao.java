package com.odianyun.search.whale.data.dao;

import com.odianyun.search.whale.data.model.Rate;

import java.util.List;

/**
 * Created by juzhongzheng on 2016-08-22.
 */
public interface RateDao {
    /*
    * 查询所有商品的评分，评价数 以及好评率
    * */
    public List<Rate> queryMerchantRateByIds(List<Long> merchantIds, Integer companyId) throws Exception;
}
