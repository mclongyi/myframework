package com.longyi.csjl.tools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author ly
 * @Description SQL 执行器
 * @date 2020/9/3 10:19
 */
@Service
public class StockSqlService {

    @Autowired
    private StockSqlRepository stockSqlRepository;

    public LinkedHashMap select(String sql){
        return stockSqlRepository.select(sql);
    }


    public int update(String sql){
        return stockSqlRepository.update(sql);
    }

    public int delete(String sql){
        return stockSqlRepository.delete(sql);
    }


    public int insert(String sql){
        return stockSqlRepository.insert(sql);
    }



}    
   