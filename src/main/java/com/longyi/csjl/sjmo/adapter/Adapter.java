package com.longyi.csjl.sjmo.adapter;

/**
 * @author ly
 * @description TODO
 * @date 2020/10/27 20:07
 * @throw
 */
public class Adapter implements Target {
    private Adaptee adaptee;

    public Adapter(Adaptee adaptee){
        this.adaptee=adaptee;
    }

    @Override
    public void request() {
        adaptee.oldRequest();
    }
}
   