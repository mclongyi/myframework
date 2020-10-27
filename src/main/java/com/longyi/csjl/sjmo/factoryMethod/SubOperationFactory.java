package com.longyi.csjl.sjmo.factoryMethod;

/**
 * @author ly
 * @description TODO
 * @date 2020/10/26 20:01
 * @throw
 */
public class SubOperationFactory implements IFactory {
    @Override
    public AbstractOperation createOperate() {
        return new SubOperation();
    }
}
   