package com.longyi.stock.design.patterns.adapter;

/**
 * @author ly
 * @Description TODO
 * @date 2020/5/28 12:54
 */
public class ConcreteTarget implements Target {
    @Override
    public void request() {
        System.out.println("concreteTarget目标方法");
    }
}
   