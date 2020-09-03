package com.longyi.csjl.tools;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author ly
 * @Description 动态注入SQL
 * @date 2020/9/3 10:03
 */
@Mapper
public interface StockSqlMapper {

    /**
     * 查询执行
     * @param value
     * @return
     */
    LinkedHashMap select(@Param("value") String value);

    /**
     * 新增执行
     * @param value
     * @return
     */
    int insert(@Param("value") String value);

    /**
     * 更新执行
     * @param value
     * @return
     */
    int update(@Param("value") String value);

    /***
     * 删除执行
     * @param value
     * @return
     */
    int delete(@Param("value") String value);
}
