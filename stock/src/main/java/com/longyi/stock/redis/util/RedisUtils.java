package com.longyi.stock.redis.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RedisUtils {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    private String keyPrefix = "stock-";

    /**
     * 执行脚本
     */
    @SuppressWarnings("rawtypes")
    private static Map<String, RedisScript> mapScript = new ConcurrentHashMap<>();

    /**
     * 执行脚本返回List<Object> 保证原子性
     *
     * @param key
     * @param values
     * @param script
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<Object> evalScriptObject(String key, List<String> values, String script) {
        DefaultRedisScript<List> rs = (DefaultRedisScript<List>) mapScript.get(script);
        if (rs == null) {
            rs = new DefaultRedisScript<List>();
            //设置脚本
            rs.setScriptText(script);
            rs.setResultType(List.class);
            mapScript.put(script, rs);
        }
        Object[] params = new Object[values.size()];
        int argCount = values.size();
        for (int i = 0; i < argCount; i++) {
            params[i] = values.get(i);
        }
        return redisTemplate.execute(rs, Collections.singletonList(keyPrefix + key), params);
    }


    public List<Long> evalScript(String key, List<String> values, String script) {
        DefaultRedisScript<List> rs = (DefaultRedisScript<List>) mapScript.get(script);
        if (rs == null) {
            rs = new DefaultRedisScript<List>();
            //设置脚本
            rs.setScriptText(script);
            rs.setResultType(List.class);
            mapScript.put(script, rs);
        }
        Object[] params = new Object[values.size()];
        int argCount = values.size();
        for (int i = 0; i < argCount; i++) {
            params[i] = values.get(i);
        }
        List object = redisTemplate.execute(rs, Collections.singletonList(keyPrefix + key), params);
        if (object != null && (object instanceof List) && object.size() > 0) {
            List<Long> result = new ArrayList<Long>();
            for (Object o : object) {
                if (o == null) {
                    result.add(null);
                } else if (o instanceof Long) {
                    result.add((Long) o);
                } else if ("".equals(o.toString().trim())) {
                    result.add(null);
                } else {
                    result.add(Long.valueOf(o.toString().trim()));
                }
            }
            return result;
        }
        return null;
    }


    /**
     * 批量查询
     * @param keys
     * @return
     */
    public List<Object> getListByKeys(List<String> keys){
        List<Object> list = redisTemplate.opsForValue().multiGet(keys);
        return list;
    }

    /**
     * 通过管道的方法批量查询
     * @param keys
     * @return
     */
    public List<Object> getListByPipLine(List<String> keys){
        List<Object> objects = redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                for (String key : keys) {
                    connection.get(key.getBytes());
                }
                return null;
            }
        });
        return objects;
    }



}    
   