package com.longyi.csjl.datastructure;

import org.w3c.dom.Node;

/**
 * @author ly
 * @description 通过连表实现队列
 * @date 2021/1/22 22:34
 * @throw
 */
public class LinkedQueue<E> implements MyQueue<E> {

    private Node head,tail;
    private int size;

    private class Node{
        private E e;
        private Node next;
        public Node(E e, Node next){
            this.e=e;
            this.next=next;
        }
        public Node(E e){
            this(e,null);
        }

        public Node(Node next){
            this(null,next);
        }

        @Override
        public String toString() {
            return e.toString();
        }
    }


    @Override
    public void enQueue(E e) {
        if(tail == null){
            tail=new Node(e);
            head=tail;
        }else{
            tail.next=new Node(e);
            tail=tail.next;
        }
        size++;
    }

    @Override
    public E deQueue() {
        if(isEmpty()){
            throw new IllegalArgumentException("can deQueue");
        }
        Node retNode=head;
        head=head.next;
        retNode.next=null;
        if(head==null){
            tail=null;
        }
        size--;
        return retNode.e;
    }

    @Override
    public E getFront() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return size==0;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public int getCapacity() {
        return getCapacity();
    }



}
   