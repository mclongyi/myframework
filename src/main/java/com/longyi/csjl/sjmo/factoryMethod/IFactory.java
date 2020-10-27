package com.longyi.csjl.sjmo.factoryMethod;

/**
 * @author ly
 * @description TODO
 * @date 2020/10/26 19:47
 */
public interface IFactory {
    /**
     * 创建操作
     * @return
     */
    AbstractOperation createOperate();
}
