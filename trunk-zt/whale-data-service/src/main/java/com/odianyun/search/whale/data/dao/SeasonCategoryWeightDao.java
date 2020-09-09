package com.odianyun.search.whale.data.dao;

import com.odianyun.search.whale.data.model.SeasonCategoryWeight;

import java.util.List;

/**
 * Created by zengfenghua on 16/11/4.
 */
public interface SeasonCategoryWeightDao {

    public List<SeasonCategoryWeight> queryAllSeasonCategoryWeight(int companyId);
}
