package com.odianyun.search.whale.data.service;

/**
 * Created by zengfenghua on 16/11/4.
 */
public interface SeasonCategoryWeightService {

     // 根据后台类目id获取一个季节权重
     public int getWeight(int companyId,long categoryId);
}
