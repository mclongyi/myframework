package com.longyi.csjl.datastructure;

/**
 * @author ly
 * @description 手写队列 ---从一端进 从同一端出 先进先出
 * @date 2021/1/21 22:26
 */
public interface MyQueue<E> {

    /**
     * 入队
     * @param e
     */
    void enQueue(E e);

    /**
     * 出队
     * @return
     */
    E deQueue();

    /**
     * 获取队列首元素
     * @return
     */
    E getFront();

    /**
     * 判断队列是否为空
     * @return
     */
    boolean isEmpty();

    /**
     * 获取元素大小
     * @return
     */
    int  getSize();

    /**
     * 获取队列容量
     * @return
     */
    int getCapacity();
}
