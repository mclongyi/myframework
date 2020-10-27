package com.longyi.csjl.sjmo.abstractFactory;

/**
 * @author ly
 * @description TODO
 * @date 2020/10/26 19:23
 * @throw
 */
public class BenzBusinessCar implements BenzCar {
    @Override
    public void gasUp() {
        System.out.println("给奔驰商务车加一般的汽油");
    }
}
   