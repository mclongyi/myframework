package com.longyi.csjl.sjmo.factoryMethod;

/**
 * @author ly
 * @description TODO
 * @date 2020/10/26 20:02
 * @throw
 */
public class TestMain {

    public static void main(String[] args) {
        IFactory factory=new AddOperateFactory();
        AbstractOperation operate = factory.createOperate();
        operate.setValue1(10);
        operate.setValue2(20);
        System.out.println(operate.result());
    }
}    
   