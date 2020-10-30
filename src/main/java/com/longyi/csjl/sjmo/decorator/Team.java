package com.longyi.csjl.sjmo.decorator;

import java.math.BigDecimal;

/**
 * @author ly
 * @description TODO
 * @date 2020/10/30 16:12
 * @throw
 */
public class Team extends CondimentAbstractDecorator {

    private BeverageAbstractComponent beverageAbstractComponent;

    public Team(BeverageAbstractComponent beverageAbstractComponent){
        this.beverageAbstractComponent=beverageAbstractComponent;
    }

    @Override
    public String getDesc() {
        return beverageAbstractComponent.getDesc()+"添加: green team";
    }

    @Override
    public BigDecimal cost() {
        return beverageAbstractComponent.cost().add(BigDecimal.valueOf(1));
    }
}
   