package com.longyi.csjl.datastructure;

/**
 * @author ly
 * @description 手写栈  从一端入 从另一端出 后进先出
 * @date 2021/1/21 9:13
 * @throw
 */
public interface MyStack<E> {

    /**
     * 入栈操作
     * @param e
     */
    void push(E e);

    /**
     * 出栈
     * @param e
     */
    E pop();

    /**
     * 是否为空
     * @return
     */
    boolean isEmpty();

    /**
     * 包含
     * @param e
     * @return
     */
    int  getSize();


    /**
     * 获取最后一个元素
     * @return
     */
    E peek();
}    
   