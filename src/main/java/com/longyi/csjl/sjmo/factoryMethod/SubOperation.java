package com.longyi.csjl.sjmo.factoryMethod;

import lombok.Data;

/**
 * @author ly
 * @description TODO
 * @date 2020/10/26 20:00
 * @throw
 */
@Data
public class SubOperation extends AbstractOperation {


    @Override
    public int result() {
        return getValue1()-getValue2();
    }
}
   