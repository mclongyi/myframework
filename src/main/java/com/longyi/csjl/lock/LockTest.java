package com.longyi.csjl.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockTest {

    private static Lock lock=new ReentrantLock();

  public static void main(String[] args) {
      lock.lock();
      try{
         System.out.println("执行复杂业务");
      }catch (Exception e){
        System.out.println("error");
      }finally{
          lock.unlock();
      }
  }
}
