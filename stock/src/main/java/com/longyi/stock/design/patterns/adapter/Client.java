package com.longyi.stock.design.patterns.adapter;

/**
 * @author ly
 * @Description TODO
 * @date 2020/5/28 12:56
 */
public class Client {
    public static void main(String[] args) {
        Target target = new ConcreteTarget();
        target.request();

        Target adapterRequest = new Adapter();
        adapterRequest.request();

    }
}    
   