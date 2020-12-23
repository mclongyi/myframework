package com.longyi.csjl.frame.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 介绍一些常用的redis实现分布式锁的方式
 */
@Component
@Slf4j
public class LockUtil {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @Autowired
    private RedisUtils redisUtils;

    private String keyPrefix = "stock-lock";

    private static final String DELIMITER = "|";


    /**
     * 通过redisTemplate的execute方式的实现分布式锁
     * @param key
     * @param clientId
     * @param time
     * @return
     */
    public boolean getLock(String key,final String clientId,Long time){
        final long milliseconds=time*1000;
        Boolean success = redisTemplate.execute((RedisCallback<Boolean>) connect ->
                connect.set((keyPrefix + key).getBytes(), ((System.currentTimeMillis() + milliseconds) + DELIMITER + clientId).getBytes(),
                        Expiration.from(time, TimeUnit.SECONDS), RedisStringCommands.SetOption.SET_IF_ABSENT));
        if(!success){
            Object object = redisTemplate.opsForValue().get(keyPrefix + key);
            if(Objects.nonNull(object)){
                String[] res = object.toString().split("\\|");
                //判断key是否过期
                if(Long.valueOf(res[0]+1) <= System.currentTimeMillis()){
                    //过期的话删除原始的key
                    redisTemplate.delete(keyPrefix + key);
                    //重新获取的锁
                    success=redisTemplate.execute((RedisCallback<Boolean>) connect ->
                            connect.set((keyPrefix + key).getBytes(), ((System.currentTimeMillis() + milliseconds) + DELIMITER + clientId).getBytes(),
                                    Expiration.from(time, TimeUnit.SECONDS), RedisStringCommands.SetOption.SET_IF_ABSENT));
                return success;
                }
            }
        }
        return success;
    }


    /**
     * 通过setIfAbsent 和getAndSet指令实现分布式锁
     * @param key
     * @param value
     * @return
     */
    public boolean getLock(String key,String value){
        Boolean success = redisTemplate.opsForValue().setIfAbsent(key, value);
        if(success){
            log.info("获取分布式锁成功");
            return success;
        }
        String currVal = (String)redisTemplate.opsForValue().get(key);
        if(!StringUtils.isEmpty(currVal) && Long.parseLong(currVal) <System.currentTimeMillis()){
            String oldVal = (String) redisTemplate.opsForValue().getAndSet(key, value);
            return !StringUtils.isEmpty(oldVal) && oldVal.equals(currVal);
        }
        return false;
    }



}
