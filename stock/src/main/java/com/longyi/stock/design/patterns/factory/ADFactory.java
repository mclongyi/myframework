package com.longyi.stock.design.patterns.factory;

/**
 * @author leiyi
 * @Description TODO
 * @date 2020/5/24
 */
public class ADFactory extends AbstractFactory {
    @Override
    public AbstractProduceCar createCar() {
        return new ADProduce();
    }
}
   