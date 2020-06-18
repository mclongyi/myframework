package com.longyi.stock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.longyi.stock.service.QueryStockService;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime) // 测试方法平均执行时间
@OutputTimeUnit(TimeUnit.MILLISECONDS) // 输出结果的时间粒度为微秒
@State(Scope.Thread)
public class AppTest {
    private ConfigurableApplicationContext context;

    final Gson gson = new Gson();
    ObjectMapper mapper = new ObjectMapper();
    QueryStockService queryStockService;

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder().include(AppTest.class.getName()+".*")
                .warmupIterations(1).measurementIterations(20).forks(2).build();
        new Runner(options).run();
    }

    /**
     * setup初始化容器的时候只执行一次
     */
    @Setup(Level.Trial)
    public void init(){
        context = SpringApplication.run(StockApplication.class);
        queryStockService = context.getBean(QueryStockService.class);
    }

    @Benchmark
    public void testGetPojo()
    {
       queryStockService.queryStock("333434");
    }
}
