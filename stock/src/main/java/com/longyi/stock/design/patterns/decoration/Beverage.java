package com.longyi.stock.design.patterns.decoration;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author leiyi
 * @Description 饮料抽象类
 * @date 2020/5/22
 */
@Data
public abstract class Beverage {

    private String description;

    /**
     * 咖啡费用计算方式
     *
     * @return
     */
    public abstract BigDecimal cost();

}    
   