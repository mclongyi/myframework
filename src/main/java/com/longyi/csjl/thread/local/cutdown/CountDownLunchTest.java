package com.longyi.csjl.thread.local.cutdown;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.SneakyThrows;

import java.util.concurrent.*;

/**
 * @author ly
 * @description TODO
 * @date 2020/10/15 15:06
 * @throw
 */
public class CountDownLunchTest {
    private static ThreadFactory threadFactory= new ThreadFactoryBuilder().
            setNameFormat("air-plan-").
            build();

    private static ThreadPoolExecutor executor=new ThreadPoolExecutor(4,4,0,
            TimeUnit.SECONDS,new ArrayBlockingQueue(10),
            threadFactory,new ThreadPoolExecutor.CallerRunsPolicy());
   private static   CountDownLatch latch = new CountDownLatch(3);

    @SneakyThrows
    public static void main(String[] args) {
        executor.execute(new AirPlan(latch,"北京航空公司",1));
        executor.execute(new AirPlan(latch,"武汉航空公司",2));
        executor.execute(new AirPlan(latch,"南京航空公司",3));
        executor.execute(new AirPlan(latch,"上海航空公司",4));
        latch.await();
        System.out.println("所有飞机一架落地");
        System.out.println("机场工作人员进场");
        executor.shutdown();
    }

}
