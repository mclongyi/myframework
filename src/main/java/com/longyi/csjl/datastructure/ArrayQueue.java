package com.longyi.csjl.datastructure;

/**
 * @author ly
 * @description 基于数组的队列
 * @date 2021/1/21 22:29
 * @throw
 */
public class ArrayQueue<E> implements MyQueue<E> {

    private MyArray<E> queue;

    public ArrayQueue(int capacity){
        queue=new MyArray<>(capacity);
    }

    public ArrayQueue(){
        queue=new MyArray<>();
    }

    @Override
    public void enQueue(E e) {
         queue.addLast(e);
    }

    @Override
    public E deQueue() {
        return queue.removeFirst();
    }

    @Override
    public E getFront() {
        return queue.getFirst();
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public int getSize() {
        return queue.getSize();
    }

    @Override
    public int getCapacity() {
        return queue.getCapacity();
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("capacity is " +queue.getCapacity())
                .append(" size is "+queue.getSize())
                .append(" front  [");
        for(int i=0;i<queue.getSize();i++){
            if(i !=queue.getSize()-1){
                stringBuilder.append((E)queue.get(i)).append(",");
            }else{
                stringBuilder.append((E)queue.get(i) +"]").append(" tail ");
            }
        }
        return stringBuilder.toString();
    }
}
   