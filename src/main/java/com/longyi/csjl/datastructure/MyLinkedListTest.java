package com.longyi.csjl.datastructure;

/**
 * @author ly
 * @description TODO
 * @date 2021/1/22 18:20
 * @throw
 */
public class MyLinkedListTest {

    public static void main(String[] args) {
        MyLinkedList<Integer> linkedList=new MyLinkedList<>();
        linkedList.addFirst(10);
        System.out.println(linkedList.toString());
        linkedList.add(33,1);
        System.out.println(linkedList.toString());
        Integer integer = linkedList.get(1);
        System.out.println(integer);
        System.out.println(linkedList.contains(33));
        linkedList.addLast(444);
        System.out.println(linkedList.toString());
        linkedList.delete(2);
        linkedList.deleteFirst();
        System.out.println(linkedList.toString());

    }
}    
   