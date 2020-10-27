package com.longyi.csjl.sjmo.factoryMethod;

import lombok.Data;

/**
 * @author ly
 * @description TODO
 * @date 2020/10/26 20:05
 * @throw
 */
@Data
public abstract class AbstractOperation {
    private Integer value1;
    private Integer value2;

    public abstract int result();
}    
   