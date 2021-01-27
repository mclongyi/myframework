package com.longyi.csjl.datastructure;

import org.w3c.dom.Node;

/**
 * @author ly
 * @description 手写列表
 * @date 2021/1/22 16:05
 * @throw
 */
public class MyLinkedList<E> {
    /**
     * 虚拟头结点
     */
    private Node dummyHead;
    private int size;

    private class Node{
        private E e;
        private Node next;
        public Node(E e,Node next){
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

    public MyLinkedList(){
        dummyHead=new Node(null,null );
        size=0;
    }

    public int getSize(){
        return size;
    }

    public boolean isEmpty(){
        return size==0;
    }



    public void add(E e,int index){
        if(index<0 || index>size){
            throw new IllegalArgumentException("the index is illegal");
        }
            Node pre=dummyHead;
            for(int i=0;i<index;i++){
                pre=pre.next;
            }
            Node node=new Node(e);
            node.next=pre.next;
            pre.next=node;
            size++;
    }

    /**
     *在连表头部添加一个元素
     * @param e
     */
    public void addFirst(E e){
      add(e,0);
    }

    /**
     *  在连表尾部
     * @param e
     */
    public void addLast(E e){
        add(e,size);
    }

    /**
     * 查询连表中的指定索引的元素
     * @param index
     * @return
     */
    public E get(int index){
        if(index<0 || index>size){
            throw new IllegalArgumentException("the index is illegal");
        }
        Node curr=dummyHead.next;
        for(int i=0;i<index;i++){
            curr=curr.next;
        }
        return curr.e;
    }

    /**
     * 获取第一个连表中的元素
     * @return
     */
    public E getFirst() {
        return get(0);
    }

    /**
     * 获取最后一个元素
     * @return
     */
    public E getLast(){
        return get(size-1);
    }

    /**
     *  更新指定位置的元素
     * @param index
     * @param e
     */
    public void update(int index,E e){
        if(index<0 || index>size){
            throw new IllegalArgumentException("the index is illegal");
        }
        Node curr=dummyHead.next;
        for(int i =0;i<index;i++){
            curr=curr.next;
        }
        curr.e=e;
    }

    /**
     * 判断连表中是否包含某个元素
     * @param e
     * @return
     */
    public boolean contains(E e){
        Node curr=dummyHead.next;
        while (curr.next!=null){
            if(curr.e.equals(e)){
                return true;
            }
            curr=curr.next;
        }
        return false;
    }


    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
         Node curr=dummyHead.next;
         while (curr!=null){
             sb.append(curr + "->");
             curr=curr.next;
         }
         sb.append("NULL");
         return sb.toString();
    }

    /**
     * 删除指定位置元素
     * @param index
     * @return
     */
    public E delete(int index){
        if(index<0 || index>size){
            throw new IllegalArgumentException("the index is illegal");
        }
        Node pre=dummyHead;
        for(int i=0;i<index;i++){
            pre=pre.next;
        }
        Node delNode=pre.next;
        pre.next=delNode.next;
        delNode.next=null;
        size--;
        return delNode.e;
    }

    /**
     * 删除第一个元素
     * @return
     */
    public E deleteFirst(){
        return delete(0);
    }


    /**
     * 删除最后一个元素
     * @return
     */
    public E deleteLast(){
        return delete(size-1);
    }
}
   