package com.longyi.csjl.datastructure;

/**
 * @author ly
 * @description TODO
 * @date 2021/1/21 22:39
 * @throw
 */
public class ArrayQueueTest {

    public static void main(String[] args) {
        ArrayQueue<String> queue=new ArrayQueue<>();
        queue.enQueue("aaa");
        queue.enQueue("bbb");
        System.out.println(queue.toString());
        queue.deQueue();
        System.out.println(queue.toString());

    }
}    
   