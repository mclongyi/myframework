package com.longyi.csjl.sjmo.proxy.cglib;

import org.springframework.cglib.proxy.Enhancer;

import java.security.MessageDigest;

/**
 * @author ly
 * @description TODO
 * @date 2020/11/2 20:35
 * @throw
 */
public class MainClient {
    public static void main(String[] args) {
        MessageInfo messageInfo=new MessageInfo();
        Enhancer enhancer=new Enhancer();
        enhancer.setSuperclass(messageInfo.getClass());
        enhancer.setCallback(new CglibProxy(messageInfo));
        MessageInfo msgProxy  =(MessageInfo)enhancer.create();
        msgProxy.sendMsg();
    }

}    
   