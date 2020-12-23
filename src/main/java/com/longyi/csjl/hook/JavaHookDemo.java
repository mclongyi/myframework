package com.longyi.csjl.hook;

public class JavaHookDemo {


  public static void main(String[] args) {
    System.out.println("程序开始执行...");
    try{
     Thread.sleep(1000);
     shutDownHook();
    }catch (Exception e){

    }
  }

    /**
     * 钩子函数 一般用作释放系统资源
     */
  public static void shutDownHook(){
    Runtime.getRuntime()
        .addShutdownHook(new Thread(() -> System.out.println("执行钩子函数")));
  }
}
