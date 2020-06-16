package com.longyi.stock.design.patterns.strategy;

/**
 * @author longyi
 * @Description TODO
 * @date 2020/5/19
 */
public class ChinaStrategy extends AbstractStrategy {
    @Override
    public void sayHello() {
        System.out.println("I am a chinese people...");
    }
}
   