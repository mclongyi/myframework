package com.longyi.csjl.sjmo.abstractFactory;

/**
 * @author ly
 * @description TODO
 * @date 2020/10/26 19:44
 * @throw
 */
public class NormalCarFactory implements CarFactory {
    @Override
    public BenzCar getBenzCar() {
        return new BenzNormalCar();
    }

    @Override
    public TeslaCar gentTeslaCar() {
        return null;
    }
}
   