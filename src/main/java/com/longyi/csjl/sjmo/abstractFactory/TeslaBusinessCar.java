package com.longyi.csjl.sjmo.abstractFactory;

/**
 * @author ly
 * @description TODO
 * @date 2020/10/26 19:24
 * @throw
 */
public class TeslaBusinessCar implements TeslaCar {
    @Override
    public void charge() {
        System.out.println("给特斯拉商务车加满油");
    }
}
   