package com.longyi.csjl.tools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;

/**
 * @author ly
 * @Description SQL执行器
 * @date 2020/9/3 10:11
 */
@Repository
public class StockSqlRepository {

    @Autowired
    private StockSqlMapper stockSqlMapper;

    public LinkedHashMap select(String sql){
        return stockSqlMapper.select(sql);
    }

    public int update(String sql){
        return stockSqlMapper.update(sql);
    }


    public int delete(String sql){
        return stockSqlMapper.delete(sql);
    }


    public int insert(String sql){
        return stockSqlMapper.insert(sql);
    }


}    
   