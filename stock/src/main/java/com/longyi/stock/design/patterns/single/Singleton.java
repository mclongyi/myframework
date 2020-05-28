package com.longyi.stock.design.patterns.single;

/**
 * @author ly
 * @Description TODO
 * @date 2020/5/25
 */
public class Singleton {
    private static Singleton instance;

    private Singleton(){}

    //懒汉模式
    public static Singleton getInstance(){
        if(instance==null){
            instance=new Singleton();
        }
        return instance;
    }

}    
   