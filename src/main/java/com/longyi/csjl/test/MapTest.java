package com.longyi.csjl.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author ly
 * @Description TODO
 * @date 2020/8/11 12:39
 */
public class MapTest {
  public static void main(String[] args) {
      Map map=new HashMap(10);
      map.put("222","112");
      map.put("3434","1132");
      Set set = map.keySet();



    System.out.println(set.toArray());
  }
}    
   