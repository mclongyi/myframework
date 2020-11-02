package com.longyi.csjl.sjmo.proxy.cglib;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author ly
 * @description TODO
 * @date 2020/11/2 19:59
 * @throw
 */
public class CglibProxy implements MethodInterceptor {
    private Object object;

    public  CglibProxy(Object object){
        this.object=object;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Object  obj=null;
        if(this.connect()){
            obj=method.invoke(this.object,objects);
            this.close();
        }
        return obj;
    }


    public boolean connect(){
        System.out.println("【消息代理】开始连接消息发送通道");
        return true;
    }

    public boolean close(){
        System.out.println("【消息代理】连接通道关闭");
        return false;
    }
}
   