package com.longyi.csjl.test;

import java.util.TreeSet;

/**
 * @author ly
 * @Description TODO
 * @date 2020/8/14 16:08
 */
public class TreeSetTest {

    public static void main(String[] args) {
        TreeSet<PersonT> treeSet=new TreeSet<>();
        treeSet.add(new PersonT(12,"赵无极"));
        treeSet.add(new PersonT(20,"李叔叔"));
        treeSet.add(new PersonT(23,"李叔叔"));
        treeSet.add(new PersonT(12,"张三丰"));
        treeSet.add(new PersonT(20,"李叔叔"));

        treeSet.forEach(
            x -> {
              System.out.println(x);
            });
  }
}    
   