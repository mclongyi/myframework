package com.longyi.stock.design.patterns.decoration;

import java.math.BigDecimal;

/**
 * @author leiyi
 * @Description TODO
 * @date 2020/5/22
 */
public class HouseBlend extends Beverage {

    public HouseBlend(){
        setDescription("混合咖啡");
    }

    @Override
    public BigDecimal cost() {
        return BigDecimal.valueOf(0.89);
    }
}
   