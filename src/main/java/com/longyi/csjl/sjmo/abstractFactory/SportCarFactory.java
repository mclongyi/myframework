package com.longyi.csjl.sjmo.abstractFactory;

/**
 * @author ly
 * @description TODO
 * @date 2020/10/26 19:26
 * @throw
 */
public class SportCarFactory implements CarFactory {
    @Override
    public BenzCar getBenzCar() {
        return new BenzSportCar();
    }

    @Override
    public TeslaCar gentTeslaCar() {
        return new TeslaSportCar();
    }
}
   