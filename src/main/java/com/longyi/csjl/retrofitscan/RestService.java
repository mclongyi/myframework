package com.longyi.csjl.retrofitscan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.transform.Result;

/**
 * @author ly
 * @Description TODO
 * @date 2020/8/12 9:48
 */
@Service
public class RestService {

    @Autowired
    private HttpApi httpApi;

    public Response  getWarehouseInfo(String factoryCode,Integer type){
        return httpApi.queryRealWarehouseByFactoryCodeAndRealWarehouseType(factoryCode, type);
    }

}    
   