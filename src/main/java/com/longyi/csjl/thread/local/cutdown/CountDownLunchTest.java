package com.longyi.csjl.thread.local.cutdown;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.List;
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
   private static   CountDownLatch latch = new CountDownLatch(4);

    @SneakyThrows
    public static void main(String[] args) {
        List<String> names = Arrays.asList("北京航空公司", "武汉航空公司", "南京航空公司", "上海航空公司");
        for(int i=4;i>0;i--){
            executor.execute(new AirPlan(latch,   names.get(i % 4),i));
        }
        latch.await();
        System.out.println("所有飞机一架落地");
        System.out.println("机场工作人员进场");
        executor.shutdown();

    }

}
