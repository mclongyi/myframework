package com.longyi.csjl.datastructure;

/**
 * @author ly
 * @description TODO
 * @date 2021/1/22 19:11
 * @throw
 */
public class LinkedStack<E> implements MyStack<E> {

    private MyLinkedList<E> linkedList;

    public LinkedStack(){
        linkedList=new MyLinkedList<>();
    }



    @Override
    public void push(E e) {
            linkedList.addFirst(e);
    }

    @Override
    public E pop() {
        return linkedList.deleteFirst();
    }

    @Override
    public boolean isEmpty() {
        return linkedList.isEmpty();
    }

    @Override
    public int getSize() {
        return linkedList.getSize();
    }

    @Override
    public E peek() {
        return linkedList.getFirst();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("stack top ");
        stringBuilder.append(linkedList);
        return stringBuilder.toString();

    }

    public static void main(String[] args) {
        LinkedStack<Integer> linkedStack=new LinkedStack<>();
        for(int i=0;i<5;i++){
            linkedStack.push(i);
            System.out.println(linkedStack.toString());
        }
        linkedStack.pop();
        System.out.println(linkedStack.toString());
    }

}
   