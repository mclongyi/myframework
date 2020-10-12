package com.longyi.csjl.proxy.jdk;

import java.lang.reflect.Proxy;
import java.math.BigDecimal;

/**
 * @author ly
 * @Description TODO
 * @date 2020/7/14 10:16
 */
public class ProxyFactory {

    /**
     * chuangj
      * @return
     */
  public LDHStartProxy createInstance(){
      LDHStartProxy ldhStartProxy=new LDHProxyService();
      LDHProxyInvocationHandler handler=new LDHProxyInvocationHandler(ldhStartProxy);
      LDHStartProxy proxy=(LDHStartProxy)Proxy.newProxyInstance(ldhStartProxy.getClass().getClassLoader(),ldhStartProxy.getClass().getInterfaces(),handler);
      return proxy;
  }

  public static void main(String[] args) {
      ProxyFactory factory=new ProxyFactory();
      LDHStartProxy instance = factory.createInstance();
      instance.dissShow("zzz", BigDecimal.valueOf(22.30));
  }
}    
   