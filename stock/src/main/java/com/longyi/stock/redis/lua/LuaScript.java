package com.longyi.stock.redis.lua;

import java.util.ArrayList;
import java.util.List;

public class LuaScript {

    /**
     * 实仓，增加库存，真实库存，库存记录不存在时，脚本
     */
    public static final String SCRIPT_ADD_INCREASE_REALQTY=
            "local ra={}; " +
            "ra[1]=1; " +
            "ra[2]=redis.call('HINCRBY',KEYS[1],ARGV[2],ARGV[1]); " + //真实库存+
            "ra[3]=redis.call('HINCRBY',KEYS[1],ARGV[3],ARGV[1]); " + //可售库存+
            "redis.call('HSET',KEYS[1],ARGV[4],ARGV[5]); " +       //库存id 设置
            "redis.call('HSET',KEYS[1],ARGV[6],ARGV[7]); " +       //虚拟仓信息json字符串 设置
            "return ra; ";


    /**
     * 虚仓，最大可能冻结库存，真实库存，脚本
     */
    public static final String SCRIPT_MAX_ABLE_LOCKQTY="if redis.call('EXISTS',KEYS[1]) == 1 then " +
            "local s= redis.call('HINCRBY',KEYS[1],ARGV[1],ARGV[2]); " +
            "local ra={}; " +
            "if s<0 then  " +
            "ra[1]=1; " +
            "ra[2]=s; " +
            "ra[3]=redis.call('HINCRBY',KEYS[1],ARGV[1],s*(-1)); " +
            "ra[4]=redis.call('HINCRBY',KEYS[1],ARGV[4],s-ARGV[2]); " +
            "ra[5]=redis.call('HGET',KEYS[1],ARGV[5]); " +
            "ra[6]=redis.call('HGET',KEYS[1],ARGV[6]); " +    //虚拟仓对应的真实仓id
            "ra[7]=redis.call('HGET',KEYS[1],ARGV[7]); " +    //虚拟仓组id
            "return ra; " +
            "else " +
            "ra[1]=2; " +
            "ra[2]=s; " +
            "ra[3]=redis.call('HINCRBY',KEYS[1],ARGV[4],ARGV[3]); " +
            "ra[4]=redis.call('HGET',KEYS[1],ARGV[5]); " +    //操作id
            "ra[5]=redis.call('HGET',KEYS[1],ARGV[6]); " +    //虚拟仓对应的真实仓id
            "ra[6]=redis.call('HGET',KEYS[1],ARGV[7]); " +    //虚拟仓组id
            "return ra; " +
            "end " +
            "else " +
            "return {0}; " +
            "end";


    public static String unlockStr ="if redis.call('EXISTS',KEYS[1]) == 1 then " +
            "if string.sub(redis.call('get', KEYS[1]),ARGV[2]) == ARGV[1] " +
            "then return {redis.call('del', KEYS[1])} else return {0} end " +
            "else return {0} end";


}    
   