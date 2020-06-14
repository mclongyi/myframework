package com.longyi.stock.ali;


import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;


/**
 * @author ly
 * @Description TODO
 * @date 2020/5/30 23:17
 */
public class ThreadPoolTest {

    /**
     * 方案1
     */
    private static ExecutorService threadPoolExecutor =new ThreadPoolExecutor(10,10,60, TimeUnit.SECONDS,new ArrayBlockingQueue<>(10));

    private static ThreadFactory threadFactory=new ThreadFactoryBuilder().setNameFormat("demo-pool-%d").build();

    /**
     * 方案2
     */
    private static ExecutorService threadpool=new ThreadPoolExecutor(10,20,0L,TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(1024),threadFactory,new ThreadPoolExecutor.AbortPolicy());


  public static void main(String[] args) {
    //阿里创建线程池的标准做法


  }
}    
   