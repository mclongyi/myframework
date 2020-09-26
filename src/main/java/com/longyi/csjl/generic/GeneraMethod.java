package com.longyi.csjl.generic;

/**
 * @author Administrator
 */
public class GeneraMethod {


    /**
     * 定义泛型方法
     * @param tClass
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public <T> T getnGenraMethod(Class<T> tClass) throws IllegalAccessException, InstantiationException {
        T t = tClass.newInstance();
        return t;
    }
 }
