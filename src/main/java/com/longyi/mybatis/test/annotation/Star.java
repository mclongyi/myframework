package com.longyi.mybatis.test.annotation;

import java.lang.annotation.*;

/**
 * @author Lenovo
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Star {

    String value() default "best";
}
