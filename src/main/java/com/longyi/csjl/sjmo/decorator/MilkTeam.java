package com.longyi.csjl.sjmo.decorator;

import java.math.BigDecimal;

/**
 * @author ly
 * @description 具体组件
 * @date 2020/10/30 16:49
 * @throw
 */
public class MilkTeam extends BeverageAbstractComponent {

    @Override
    public String getDesc() {
        return"奶茶";
    }

    @Override
    public BigDecimal cost() {
        return BigDecimal.valueOf(5);
    }
}
   