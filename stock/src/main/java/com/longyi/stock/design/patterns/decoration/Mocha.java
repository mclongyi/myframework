package com.longyi.stock.design.patterns.decoration;

import java.math.BigDecimal;

/**
 * @author leiyi
 * @Description mocha 调料
 * @date 2020/5/22
 */
public class Mocha extends CondimentDecorator {

    Beverage beverage;

    public Mocha(Beverage beverage){
        this.beverage=beverage;
    }

    @Override
    public String getDescription() {
        return beverage.getDescription()+":"+"mocha";
    }

    @Override
    public BigDecimal cost() {
        return BigDecimal.valueOf(0.2).add(beverage.cost());
    }
}
   