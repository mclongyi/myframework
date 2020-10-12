package com.longyi.csjl.lock;

/**
 * @author ly
 * @description TODO
 * @date 2020/10/12 13:25
 * @throw
 */
public class DeadLockTest {
    public static void main(String[] args) {
        DeadLockDemo t1 = new DeadLockDemo();
        DeadLockDemo t2 = new DeadLockDemo();
        DeadLockDemo.flag = 1;
        new Thread(t1).start();
        //让main线程休眠1秒钟,保证t2开启锁住o2.进入死锁
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        DeadLockDemo.flag = 0;
        new Thread(t2).start();
    }
}    
   