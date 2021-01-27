package com.longyi.csjl.datastructure;

import java.util.Arrays;

/**
 * @author ly
 * @description 循环队列
 * @date 2021/1/21 23:36
 * @throw
 */
public class MyLoopQueue<E> implements MyQueue<E> {
    private E[] data;
    private int front,tail;
    private int size;
    private int capacity;


    public MyLoopQueue(int capacity){
        data=(E[])new Object[capacity+1];
        size=0;
        capacity=0;
    }

    public MyLoopQueue(){
        this(10);
    }

    @Override
    public void enQueue(E e) {
       //判断队列是否已满
       if((tail+1)%data.length == front){
           //扩容
           resize(getCapacity()*2);
       }
       data[tail]=e;
       tail=(tail+1)%data.length;
        size++;
    }

    private void resize(int newCapacity){
        E[] newData=(E[])new Object[newCapacity+1];
        for(int i=0;i<size;i++){
            //循环队列在重新赋值时需注意
            newData[i]=data[(i+front)%data.length];
        }
        data=newData;
        front=0;
        tail=size;
    }

    @Override
    public E deQueue() {
        if(isEmpty()){
            throw  new IllegalArgumentException("queue is empty");
        }
        E e=data[front];
        data[front]=null;
        front=(front+1)%data.length;
        size--;
        return e;
    }

    @Override
    public E getFront() {
        if(isEmpty()){
            throw new IllegalArgumentException("queue is empty");
        }
        return data[front];
    }

    @Override
    public boolean isEmpty() {
        return tail==front;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public int getCapacity() {
        return capacity-1;
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(String.format("queue size is %d,capacity=%d\n",size,getCapacity()));
        stringBuilder.append("front [");
        for (int i=front;i!=tail;i=(i+1)%data.length){
            stringBuilder.append(data[i]);
            if(i != size-1){
                stringBuilder.append(",");
            }
            stringBuilder.append("] tail");
        }

        return stringBuilder.toString();
    }
}
   