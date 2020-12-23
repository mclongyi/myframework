package com.longyi.csjl.future;

import com.alibaba.fastjson.JSON;
import com.longyi.csjl.value.Student;

import java.util.concurrent.*;

/**
 * @author Administrator
 */
public class ClientTest {


  public static void main(String[] args) throws ExecutionException, InterruptedException {
      ThreadPoolExecutor executor=new ThreadPoolExecutor(10,10,0, TimeUnit.SECONDS,new ArrayBlockingQueue<>(100));
      Callable<Student> student=new FutureTaskService("测试");
      FutureTask<Student> task=new FutureTask<>(student);
      executor.execute(task);
      System.out.println("通知交易中心");
      System.out.println("保存数据库");
      if(task.isDone()){
          Student info = task.get();
          System.out.println("接受到返回信息" + JSON.toJSONString(student));
      }
  }
}
