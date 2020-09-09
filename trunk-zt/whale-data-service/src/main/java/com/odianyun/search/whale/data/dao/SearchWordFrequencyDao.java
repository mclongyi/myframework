package com.odianyun.search.whale.data.dao;

import com.odianyun.search.whale.data.model.hotword.SearchWordFrequencyVO;

import java.util.List;

/**
 * @author hs
 * @date 2018/9/13.
 */
public interface SearchWordFrequencyDao {
    /**
     * 查询某天的搜索词频数据
     * @param vo
     * @return
     */
    List<SearchWordFrequencyVO> selectByDateAndStatus(SearchWordFrequencyVO vo);


    void insertKeyword(SearchWordFrequencyVO vo);


    /**
     * 查询某天的总数
     * @param vo
     * @return
     */
    Long countByDate(SearchWordFrequencyVO vo);

    void batchUpdateStatus(List<SearchWordFrequencyVO> voList);
}
