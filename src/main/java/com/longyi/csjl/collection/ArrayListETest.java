package com.longyi.csjl.collection;



import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author ly
 * @Description TODO
 * @date 2020/8/25 12:49
 */
public class ArrayListETest {


  public static void main(String[] args) {
      ArrayList arrayList=new ArrayList();
      List<String> list = Arrays.asList("A", "A", "B", "C");
      List<String> list2 = Arrays.asList("E", "A", "F", "G");
      List<String> collate = CollectionUtils.collate(list, list2);
      System.out.println("两个集合合并"+collate);

      //通过集合求交集
      Collection<String> intersection = CollectionUtils.intersection(list, list2);
      System.out.println("交集is:"+intersection);

    // 通过从其他集合中减去一个集合的对象来获取新集合
      Collection<String> subtract = CollectionUtils.subtract(list, list2);
      System.out.println("集合相减is:"+subtract);

     //求并集
      Collection<String> union = CollectionUtils.union(list, list2);
      System.out.println("两个集合并集是:"+union);
  }
}    
   