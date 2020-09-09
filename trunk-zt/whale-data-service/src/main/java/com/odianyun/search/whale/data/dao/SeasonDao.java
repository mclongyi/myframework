package com.odianyun.search.whale.data.dao;

import com.odianyun.search.whale.data.model.Season;

import java.util.List;

/**
 * Created by zengfenghua on 16/11/4.
 */
public interface SeasonDao {

     public List<Season> queryAllSeasonData(int companyId);

}
