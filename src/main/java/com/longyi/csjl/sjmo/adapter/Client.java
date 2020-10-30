package com.longyi.csjl.sjmo.adapter;

/**
 * @author ly
 * @description TODO
 * @date 2020/10/27 20:09
 * @throw
 */
public class Client {

    public static void main(String[] args) {
        Target target=new Adapter(new Adaptee());
        target.request();
    }
}    
   