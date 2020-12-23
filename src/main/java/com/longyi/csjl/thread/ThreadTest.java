package com.longyi.csjl.thread;

public class ThreadTest {

  public static void main(String[] args) throws InterruptedException {
      MyThread myThread1=new MyThread();
      Thread thread=new Thread(myThread1);
      thread.setName("thread1");
      thread.setPriority(5);

      MyThread myThread2=new MyThread();
      Thread thread2=new Thread(myThread2);
      thread2.setPriority(2);
      thread2.setName("thread2");
      thread2.start();
      thread.start();

  }

}
