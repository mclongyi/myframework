package com.longyi.csjl.sjmo.Flyweight;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ly
 * @description TODO
 * @date 2020/11/1 16:48
 * @throw
 */
public class FlyweightFactory {

    private static ConcurrentHashMap<String,Flyweight>  pool=new ConcurrentHashMap<>();

    public static Flyweight getFlyweight(String extrinsic){
        Flyweight  flyweight=null;
        if(pool.containsKey(extrinsic)){
            flyweight=pool.get(extrinsic);
            System.out.println("池子中已存在，直接取");
        }else{
            flyweight=new ConcreteFlyweight(extrinsic);
            pool.put(extrinsic,flyweight);
            System.out.println("池子中没有 创建");
        }
        return flyweight;
    }
}    
   