package com.longyi.csjl.datastructure;

import com.longyi.csjl.thread.local.semapnoe.Student;

import java.util.ArrayList;

/**
 * @author ly
 * @description TODO
 * @date 2021/1/18 21:48
 * @throw
 */
public class MyArrayTest {

    public static void main(String[] args) {
        MyArray myArray=new MyArray();
        myArray.addFirst(1);
        myArray.addLast(10);
        myArray.add(5,1);
        myArray.add(20,3);
        System.out.println(myArray.toString());
        System.out.println(myArray.contains(1));
        System.out.println(myArray.find(20));
        myArray.removeLast();
        myArray.remove(2);
        System.out.println(myArray.toString());
        MyArray<String> list=new MyArray<>();
        list.add("aa",0);
        list.add("ccc",2);
        list.remove(2);
        System.out.println(list.toString());


    }
}    
   