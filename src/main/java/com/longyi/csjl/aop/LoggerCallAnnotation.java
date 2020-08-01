package com.longyi.csjl.aop;

import java.lang.annotation.*;

/**
 * 自定义打印日志的注解
 * @author longyi
 * @date 2020-07-30
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface LoggerCallAnnotation {

    /**
     * 唯一标识
     * @return
     */
     String recordCode();

    /**
     * 服务端ip
     * @return
     */
    String serverIp() default "127.0.0.1";

    /**
     * 是否需要存储数据库
     * @return
     */
    boolean needDB() default true;
}
