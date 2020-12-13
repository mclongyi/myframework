package com.longyi.csjl.frame.datastructure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 数组数据结构 线性数据结构
 */
public class ArrayStucture {

  public static void main(String[] args) {
      ArrayList<Integer> array=new ArrayList<>();
      List<Integer> array1 = Arrays.asList(1, 2);
      System.out.println(array.toArray().getClass() !=Object[].class);
      System.out.println(array1.toArray().getClass() !=Object[].class);

      LinkedList<Integer> linkedList=new LinkedList();
      linkedList.add(20);
      linkedList.add(30);
      linkedList.add(50);
      linkedList.add(10);
      linkedList.poll();
      linkedList.peek();
      linkedList.offer(40);
      linkedList.getFirst();
      linkedList.getLast();


  }
}
