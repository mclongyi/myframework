package com.longyi.csjl.proxy.cglib;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author ly
 * @Description TODO
 * @date 2020/8/14 17:06
 */
public class LYMessageProxy implements MethodInterceptor {
   private Object object;

   public LYMessageProxy(Object object){
       this.object=object;
   }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Object res=null;
        if(this.connect()){
            res=method.invoke(this.object,objects);
             this.close();
        }

        return res;
    }

    public boolean connect(){
        System.out.println("【消息代理】进行消息发送通道的连接。");
        return true;
    }
    public void close(){
        System.out.println("【消息代理】关闭消息通道。");
    }

    public  void initCglib(){
        Message lyMessageProxy=new Message();
       Enhancer enhancer=new Enhancer();
       enhancer.setSuperclass(lyMessageProxy.getClass());
       enhancer.setCallback(new LYMessageProxy(lyMessageProxy));
        Message messageProxy=(Message) enhancer.create();
        messageProxy.sendMsg();
    }

}
   