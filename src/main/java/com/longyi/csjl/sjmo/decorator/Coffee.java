package com.longyi.csjl.sjmo.decorator;

import java.math.BigDecimal;

/**
 * @author ly
 * @description TODO
 * @date 2020/10/30 16:15
 * @throw
 */
public class Coffee extends CondimentAbstractDecorator {

    private BeverageAbstractComponent beverageAbstractComponent;

    public Coffee(BeverageAbstractComponent beverageAbstractComponent){
        this.beverageAbstractComponent=beverageAbstractComponent;
    }

    @Override
    public String getDesc() {
        return beverageAbstractComponent.getDesc()+"：添加：coffee";
    }

    @Override
    public BigDecimal cost() {
        return beverageAbstractComponent.cost().add(BigDecimal.valueOf(2));
    }
}
   