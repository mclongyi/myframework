package com.longyi.stock.rebuild.ifelse;

import java.lang.annotation.*;

/**
 * @author Lenovo
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface OrderHandlerType {
    int value() default 0;
}
