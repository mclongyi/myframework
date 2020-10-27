package com.longyi.csjl.sjmo.abstractFactory;

/**
 * @author ly
 * @description TODO
 * @date 2020/10/26 19:23
 * @throw
 */
public class BenzSportCar implements BenzCar{
    @Override
    public void gasUp() {
        System.out.println("给奔驰加最好的汽油");
    }
}
   