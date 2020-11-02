package com.longyi.csjl.sjmo.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author ly
 * @description TODO
 * @date 2020/11/1 19:59
 * @throw
 */
public class ProxyHandler implements InvocationHandler {

    private Object object;

    public ProxyHandler(Object object){
        this.object=object;
    }



    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("我是代理人");
        Object invoke = method.invoke(object, args);
        System.out.println("代理人处理事情完成"+invoke);
        return invoke;
    }
}
   