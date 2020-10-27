package com.longyi.csjl.sjmo.factoryMethod;

/**
 * @author ly
 * @description TODO
 * @date 2020/10/26 19:55
 * @throw
 */
public class AddOperateFactory implements IFactory {

    @Override
    public AbstractOperation createOperate() {
        return new AddOperation();
    }
}
   