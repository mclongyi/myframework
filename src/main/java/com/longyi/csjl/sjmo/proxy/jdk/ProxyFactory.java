package com.longyi.csjl.sjmo.proxy.jdk;

import java.lang.reflect.Proxy;

/**
 * @author ly
 * @description TODO
 * @date 2020/11/1 20:19
 * @throw
 */
public class ProxyFactory {

    public static void main(String[] args) {
        UserService userService=new UserServiceImpl();
        ProxyHandler proxyHandler=new ProxyHandler(userService);
        UserService userServiceProxy=(UserService)Proxy.newProxyInstance(userService.getClass().getClassLoader(),userService.getClass().getInterfaces(),proxyHandler);
        userServiceProxy.userSay("hah");
    }


}    
   