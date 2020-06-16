package com.longyi.stock.design.patterns.decoration;

/**
 * @author leiyi
 * @Description 调料基类
 * @date 2020/5/22
 */
public abstract class CondimentDecorator extends Beverage {
    /**
     * 获取描述
     *
     * @return
     */
    @Override
    public abstract String getDescription();

}    
   