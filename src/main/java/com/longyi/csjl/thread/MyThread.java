package com.longyi.csjl.thread;

public class MyThread extends Thread {

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+"开始发射火箭...");
    }
}
