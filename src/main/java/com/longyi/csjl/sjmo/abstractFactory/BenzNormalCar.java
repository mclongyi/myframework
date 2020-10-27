package com.longyi.csjl.sjmo.abstractFactory;

/**
 * @author ly
 * @description TODO
 * @date 2020/10/26 19:42
 * @throw
 */
public class BenzNormalCar implements BenzCar {
    @Override
    public void gasUp() {
        System.out.println("普通奔驰可以油电混用");
    }
}
   