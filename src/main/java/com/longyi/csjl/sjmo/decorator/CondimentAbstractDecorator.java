package com.longyi.csjl.sjmo.decorator;

/**
 * @author ly
 * @description 抽象装饰者
 * @date 2020/10/28 19:50
 * @throw
 */
public abstract class CondimentAbstractDecorator extends BeverageAbstractComponent {


    /**
     * 装饰者描述信息
     * @return
     */
    @Override
    public abstract String getDesc();
}
   