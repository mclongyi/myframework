package com.longyi.csjl.sjmo.abstractFactory;

/**
 * @author ly
 * @description TODO
 * @date 2020/10/26 19:29
 * @throw
 */
public class BussCarFactory implements CarFactory {
    @Override
    public BenzCar getBenzCar() {
        return new BenzBusinessCar();
    }

    @Override
    public TeslaCar gentTeslaCar() {
        return new TeslaBusinessCar();
    }
}
   