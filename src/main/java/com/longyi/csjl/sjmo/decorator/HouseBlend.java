package com.longyi.csjl.sjmo.decorator;

import java.math.BigDecimal;

/**
 * @author ly
 * @description 具体组件
 * @date 2020/10/30 16:45
 * @throw
 */
public class HouseBlend  extends BeverageAbstractComponent{
    @Override
    public String getDesc() {
        return "黑咖啡";
    }

    @Override
    public BigDecimal cost() {
        return BigDecimal.valueOf(3);
    }
}
   