package com.odianyun.search.whale.data.dao.impl;

import com.odianyun.search.whale.data.dao.SeasonCategoryWeightDao;
import com.odianyun.search.whale.data.model.SeasonCategoryWeight;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import java.util.List;

/**
 * Created by zengfenghua on 16/11/4.
 */
public class SeasonCategoryWeightDaoImpl extends SqlMapClientDaoSupport implements SeasonCategoryWeightDao {

    @Override
    public List<SeasonCategoryWeight> queryAllSeasonCategoryWeight(int companyId) {
        return getSqlMapClientTemplate().queryForList("queryAllSeasonCategoryWeight",companyId);
    }
}
