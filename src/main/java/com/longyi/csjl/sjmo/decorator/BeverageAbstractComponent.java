package com.longyi.csjl.sjmo.decorator;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ly
 * @description 抽象组件
 * @date 2020/10/28 19:48
 * @throw
 */
@Data
public abstract class BeverageAbstractComponent {

    private String desc;

    /**
     * 价格信息
     * @return
     */
    public abstract BigDecimal cost();
}    
   