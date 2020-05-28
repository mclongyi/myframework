package com.longyi.stock.design.patterns.single;

/**
 * @author ly
 * @Description TODO
 * @date 2020/5/25
 */
public class SingletonByInnerClass {

    private SingletonByInnerClass(){

    }

    private static SingletonByInnerClass instance;

    public static class Instance{
        public static SingletonByInnerClass getInstance(){
            instance=new SingletonByInnerClass();
            return instance;
        }
    }
}    
   