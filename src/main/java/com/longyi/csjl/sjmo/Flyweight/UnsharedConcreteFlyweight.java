package com.longyi.csjl.sjmo.Flyweight;

/**
 * @author ly
 * @description 具体享元角色
 * @date 2020/11/1 16:46
 * @throw
 */
public class UnsharedConcreteFlyweight extends Flyweight {

    public UnsharedConcreteFlyweight(String extrinsic) {
          super(extrinsic);
    }

    @Override
    public void operate(int extrinsic) {
        System.out.println("不共享的具体Flyweight:" + extrinsic);
    }
}
   