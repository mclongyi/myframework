package com.longyi.csjl.proxy.jdk;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author ly
 * @Description TODO
 * @date 2020/7/14 9:38
 */
public class LDHProxyInvocationHandler implements InvocationHandler , Serializable {

    private Object object;
    public LDHProxyInvocationHandler(Object object){
        this.object=object;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("我的LDH代理人 有事请找我");
        Object result = method.invoke(object, args);
        System.out.println("代理人处理事情完成");
        return result;
    }

}
   