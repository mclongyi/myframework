package com.longyi.csjl.datastructure;

/**
 * @author ly
 * @description TODO
 * @date 2021/1/21 9:25
 * @throw
 */
public class ArrayStack<E> implements MyStack<E> {
    private MyArray<E> array;

    public ArrayStack(){
        array=new MyArray<>();
    }

    public ArrayStack(int capacity){
        array=new MyArray<>(capacity);
    }

    @Override
    public void push(E e) {
        array.addLast(e);
    }

    @Override
    public E pop() {
        return array.removeLast();
    }

    @Override
    public boolean isEmpty() {
        return array.isEmpty();
    }

    @Override
    public int getSize() {
        return array.getSize();
    }

    @Override
    public E peek() {
        return array.getLast();
    }
}
   