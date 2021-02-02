package com.longyi.csjl.datastructure.map;

import com.longyi.csjl.datastructure.LinkedQueue;
import org.w3c.dom.Node;

/**
 * @author ly
 * @description TODO
 * @date 2021/2/2 20:46
 * @throw
 */
public class LinkedListMap<K,V> implements MyMap<K,V> {

    private class Node{
        private K key;
        private V value;
        private Node next;

        public Node(K key,V value,Node next){
            this.key=key;
            this.value=value;
            this.next=next;
        }
        public Node(K key){
            this(key,null,null);
        }

        public Node(Node next){
            this(null,null,next);
        }

        public Node(){
            this(null,null,null);
        }

        @Override
        public String toString() {
            return key.toString()+":"+value.toString();
        }
    }

    private int size;
    private Node dummyHead;

    public LinkedListMap(){
        dummyHead=new Node();
        size=0;
    }

    @Override
    public void add(K key, V value) {
        Node node = getNode(key);
        if(node==null){
            dummyHead.next=new Node(key,value,dummyHead.next);
            size++;
        }else{
            node.value=value;
        }
    }

    private Node getNode(K key){
        Node pre=dummyHead.next;
        while (pre.next!=null){
            if(key.equals(pre.next.key)){
                return pre.next;
            }
            pre=pre.next;
        }
        return null;
    }

    @Override
    public V remove(K key) {
        Node pre=dummyHead.next;
        while (pre.next!=null){
            if(pre.next.key.equals(key)){
                break;
            }
            pre=pre.next;
        }
        if(pre.next!=null){
            Node delNode=pre.next;
            pre.next=delNode.next;
            delNode.next=null;
            size--;
            return delNode.value;
        }
        return null;
    }

    @Override
    public boolean contains(K key) {
        return getNode(key)==null;
    }

    @Override
    public V get(K key) {
        Node node = getNode(key);
        if(node!=null){
            return node.value;
        }
        return null;
    }

    @Override
    public void set(K key, V value) {
        Node node = getNode(key);
        if(node==null){
            throw new IllegalArgumentException("参数有误");
        }
        node.value=value;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size==0;
    }
}
   