package com.longyi.stock.design.patterns.decoration;

import java.math.BigDecimal;

/**
 * @author leiyi
 * @Description TODO
 * @date 2020/5/22
 */
public class Espesso extends Beverage {

    public Espesso() {
        setDescription("浓缩咖啡");
    }

    @Override
    public BigDecimal cost() {
        return BigDecimal.valueOf(1.99);
    }
}
   