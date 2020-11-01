package com.longyi.csjl.sjmo.Flyweight;

/**
 * @author ly
 * @description 具体享元角色
 * @date 2020/11/1 16:41
 * @throw
 */
public class ConcreteFlyweight extends Flyweight{



   public ConcreteFlyweight(String extrinsic){
       super(extrinsic);
   }


    @Override
    public void operate(int extrinsic) {
        System.out.println("具体Flyweight:" + extrinsic);
    }
}
   