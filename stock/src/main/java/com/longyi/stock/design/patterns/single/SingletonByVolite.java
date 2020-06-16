package com.longyi.stock.design.patterns.single;

/**
 * @author ly
 * @Description TODO
 * @date 2020/5/25
 */
public class SingletonByVolite {


    private SingletonByVolite() {
    }

    private static volatile SingletonByVolite instance;

    public static SingletonByVolite getInstance() {
        if (instance == null) {
            synchronized (SingletonByVolite.class) {
                if (instance == null) {
                    instance = new SingletonByVolite();
                }
            }
        }
        return instance;
    }

}    
   