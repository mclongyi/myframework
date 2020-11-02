package com.longyi.csjl.sjmo.proxy.st;

/**
 * @author ly
 * @description TODO
 * @date 2020/11/1 19:35
 * @throw
 */
public class ProxyTarget {

    private Target target=new Target();

    public void proxyCost(){
        System.out.println("我是代理人 有事请找我");
        target.cost();
    }
}    
   