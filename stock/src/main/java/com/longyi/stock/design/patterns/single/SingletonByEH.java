package com.longyi.stock.design.patterns.single;

/**
 * @author ly
 * @Description TODO
 * @date 2020/5/25
 */
public class SingletonByEH {
    private SingletonByEH() {

    }

    private static SingletonByEH instence = new SingletonByEH();

    //恶汉模式
    public static SingletonByEH getInstance() {
        return instence;
    }
}    
   