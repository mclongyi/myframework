package com.longyi.stock.design.patterns.decoration;

import java.math.BigDecimal;

/**
 * @author leiyi
 * @Description TODO
 * @date 2020/5/22
 */
public class Cream extends CondimentDecorator {

    private Beverage beverage;

    public Cream(Beverage beverage){
        this.beverage=beverage;
    }

    @Override
    public String getDescription() {
        return beverage.getDescription()+":cream";
    }

    @Override
    public BigDecimal cost() {
        return beverage.cost().add(BigDecimal.valueOf(1));
    }
}
   