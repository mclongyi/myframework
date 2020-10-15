package com.longyi.csjl.thread.local.semapnoe;

import java.util.concurrent.Semaphore;

/**
 * @author ly
 * @description TODO
 * @date 2020/10/15 14:07
 * @throw
 */
public class SemaphoreTest {

    static Semaphore semaphore=new Semaphore(3);

    public static void main(String[] args) {
        for(int i=0;i<10;i++){
            new Student(semaphore,"学生"+i).start();
        }
    }


}    
   