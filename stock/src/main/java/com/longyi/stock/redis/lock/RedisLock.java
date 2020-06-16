package com.longyi.stock.redis.lock;

import com.longyi.stock.redis.lua.LuaScript;
import com.longyi.stock.redis.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/***
 * 通过redis实现分布式锁的几种方式
 */
@Component
@Slf4j
public class RedisLock {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @Autowired
    private RedisUtils redisUtils;

    private String keyPrefix = "stock-lock";

    private static final String DELIMITER = "|";

    /**
     * 分布式锁定
     *
     * @param lockKey  键
     * @param clientId 客户端id
     * @param time     过期时间
     * @return
     */
    public boolean lock(String lockKey, final String clientId, long time) {
        final long milliseconds = time * 1000L;
        boolean success = redisTemplate.execute((RedisCallback<Boolean>) connection ->
                connection.set((keyPrefix + lockKey).getBytes(), ((System.currentTimeMillis() + milliseconds) + DELIMITER + clientId).getBytes(),
                        Expiration.from(time, TimeUnit.SECONDS), RedisStringCommands.SetOption.SET_IF_ABSENT));
        if (!success) {
            Object oldVal = redisTemplate.opsForValue().get(keyPrefix + lockKey);
            final String[] oldValues = oldVal.toString().split(Pattern.quote(DELIMITER));
            if (Long.parseLong(oldValues[0]) + 1 <= System.currentTimeMillis()) {
                del(lockKey);
                success = redisTemplate.execute((RedisCallback<Boolean>) connection -> connection.set((keyPrefix + lockKey).getBytes(), ((System.currentTimeMillis() + milliseconds) + DELIMITER + clientId).getBytes(), Expiration.from(time, TimeUnit.SECONDS), RedisStringCommands.SetOption.SET_IF_ABSENT));
                return success;
            }
        }
        return success;
    }

    /**
     * 获取分布式锁
     *
     * @param key
     * @param value
     * @return
     */
    public boolean lock(String key, String value) {
        if (redisTemplate.opsForValue().setIfAbsent(key, value)) {
            log.info("获取分布式锁成功");
            return true;
        }
        //根据key获取对应的value
        String curVal = (String) redisTemplate.opsForValue().get(key);
        if (StringUtils.isNotBlank(curVal) && Long.parseLong(curVal) < System.currentTimeMillis()) {
            String oldVal = (String) redisTemplate.opsForValue().getAndSet(key, value);
            return StringUtils.isNotBlank(oldVal) && oldVal.equals(curVal);
        }
        return false;
    }


    /**
     * lua脚本释放分布式锁
     *
     * @param lockKey
     * @param clientId
     * @return
     */
    public boolean unLock(String lockKey, String clientId) {
        String json = LuaScript.unlockStr;
        List<String> list = new ArrayList<>(2);
        list.add(clientId);
        list.add((-clientId.length()) + "");
        List<Long> result = redisUtils.evalScript(lockKey, list, json);
        return result != null && result.size() > 0;
    }


    public void del(String key) {
        redisTemplate.delete(keyPrefix + key);
    }

}    
   