package com.longyi.csjl.lock;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ly
 * @description 死锁排查
 * @date 2020/10/12 11:43
 * @throw
 */
@Slf4j
public class DeadLockDemo implements Runnable {

    public static int flag=1;

    static Object o1=new Object();
    static Object o2=new Object();

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+":此时 flag="+flag);
        if(flag == 1){
            synchronized (o1){
                try{
                    System.out.println("我是:"+Thread.currentThread().getName()+"锁住 o1");
                    Thread.sleep(5000);
                    System.out.println(Thread.currentThread().getName()+"醒来->准备获取 o2");
                }catch (Exception e){
                    log.error("锁定异常:{}",e);
                }
                synchronized (o2){
                    System.out.println(Thread.currentThread().getName() + "拿到 o2");
                }
            }
        }
        if(flag == 0){
            synchronized (o2){
                try{
                    System.out.println("我是" + Thread.currentThread().getName() + "锁住 o2");
                    Thread.sleep(5000);
                    System.out.println(Thread.currentThread().getName() + "醒来->准备获取 o2");
                }catch (Exception e){
                    log.error("获取锁异常:{}",e);
                }
                synchronized (o1){
                    System.out.println(Thread.currentThread().getName() + "拿到 o1");
                }
            }
        }
    }
}
   