package com.longyi.csjl.thread.local.cutdown;

import java.util.concurrent.CountDownLatch;

/**
 * @author ly
 * @description TODO
 * @date 2020/10/15 15:06
 * @throw
 */
public class AirPlan  extends Thread{
    private CountDownLatch lance;
    private String name;
    private Integer time;

    AirPlan(CountDownLatch countDownLatch,String airPlanName,Integer time){
        this.lance =countDownLatch;
        this.name=airPlanName;
        this.time=time;
    }

    @Override
    public void run() {
        try{
            System.out.println( name+"飞机正在起飞....预计"+time+"小时落地");
            Thread.sleep(1000);
        }catch (Exception e){
            System.out.println("起飞失败");
        }
        lance.countDown();
    }
}
   