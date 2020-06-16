package com.longyi.stock.redis.lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author leiyi
 * @Description TODO
 * @date 2020/5/17
 */
@Component
public class ReadSession {

    @Resource
    private RedissonClient redissonClient;


    public boolean createOrder() {
        RLock rLock = redissonClient.getLock("redisson_demo");
        boolean isLock = false;
        try {
            isLock = rLock.tryLock(10, 120, TimeUnit.SECONDS);
            if (isLock) {
                System.out.println("获取分布式锁成功...");
            } else {
                System.out.println("获取分布式锁失败...");
            }
        } catch (Exception e) {
            System.out.println("fail ...");
        } finally {
            //rLock.unlock();
        }
        return isLock;
    }


}    
   