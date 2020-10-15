package com.longyi.csjl.thread.local.semapnoe;

import java.util.concurrent.Semaphore;

/**
 * @author ly
 * @description TODO
 * @date 2020/10/15 14:11
 * @throw
 */
public class Student extends Thread {

    private Semaphore semaphore;
    private String name;

    public Student(Semaphore semaphore,String name){
        this.semaphore=semaphore;
        this.name=name;
    }

    @Override
    public void run() {
        try {
            semaphore.acquire();
            System.out.println(name+"获得许可证");
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            System.out.println(name+"释放许可");
            semaphore.release();
        }
    }
}
   