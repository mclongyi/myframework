package com.odianyun.search.whale.data.dao.impl;

import com.odianyun.search.whale.data.dao.SeasonDao;
import com.odianyun.search.whale.data.model.Season;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import java.util.List;

/**
 * Created by zengfenghua on 16/11/4.
 */
public class SeasonDaoImpl extends SqlMapClientDaoSupport implements SeasonDao {

    @Override
    public List<Season> queryAllSeasonData(int companyId) {

        return getSqlMapClientTemplate().queryForList("queryAllSeasonData",companyId);
    }
}
