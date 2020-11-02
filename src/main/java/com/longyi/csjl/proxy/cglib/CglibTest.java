package com.longyi.csjl.proxy.cglib;

import org.springframework.cglib.proxy.Enhancer;

/**
 * @author ly
 * @Description cglib的方式进行代理
 * cglib的方式是基于父类创建的代理类
 * @date 2020/8/14 17:17
 */
public class CglibTest {
  public static void main(String[] args) {
      Message lyMessageProxy=new Message();
      Enhancer enhancer=new Enhancer();
      enhancer.setSuperclass(lyMessageProxy.getClass());
      enhancer.setCallback(new LYMessageProxy(lyMessageProxy));
      Message messageProxy=(Message) enhancer.create();
      messageProxy.sendMsg();
      Message proxyMsg=(Message)enhancer.create();
      proxyMsg.sendMsg();
  }
}    
   