package com.longyi.csjl.threadfactory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;

/**
 * 优雅创建线程池的几种方式
 */
public class ThreadFacotryTest {

    /**
     * 方式1  阿里推荐方式
     */
    private ThreadFactory threadFactory=new ThreadFactoryBuilder()
            .setNameFormat("longyi-")
            .build();
    ThreadPoolExecutor executor=new ThreadPoolExecutor(20,30,0, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(100),threadFactory,new ThreadPoolExecutor.AbortPolicy());

    @Bean("asyncExecute")
    public Executor asyncExecute() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数
        executor.setCorePoolSize(5);
        // 最大线程数
        executor.setMaxPoolSize(10);
        // 配置缓存队列数
        executor.setQueueCapacity(20);
        // 配置最大空闲时间
        executor.setKeepAliveSeconds(60);
        // 前缀
        executor.setThreadNamePrefix("stock-thread-");
        // 线程池对拒绝任务的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);

        return executor;
    }



}
