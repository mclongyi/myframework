package com.longyi.stock.redis;

import com.alibaba.fastjson.JSON;
import com.longyi.stock.redis.util.RedisUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

/**
 * @author ly
 * @Description TODO
 * @date 2020/6/19 13:37
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisBatchTest {

    @Autowired
    private RedisUtils redisUtils;

    @Test
    public void testGetList(){
        List<String> list = Arrays.asList("C0043", "X001", "X0021");
        List<Object> listByKeys = redisUtils.getListByKeys(list);
        System.out.println(JSON.toJSONString(listByKeys));
    }


    @Test
    public void testGetPipLine(){
        List<String> list = Arrays.asList("C0043", "X001", "X0021");
        List<Object> listByPipLine = redisUtils.getListByPipLine(list);
        System.out.println(JSON.toJSONString(listByPipLine));
    }

}    
   