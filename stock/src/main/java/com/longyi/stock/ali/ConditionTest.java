package com.longyi.stock.ali;

import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ly
 * @Description TODO
 * @date 2020/5/30 18:39
 */
public class ConditionTest {

  public static void main(String[] args) {
//    Integer a=10;
//    Integer b=1;
//    Integer c=null;
//    boolean res=false;
//    Integer e=res?a*b:c;
//    System.out.println(e);

    Map map=new HashMap((int)(10/0.75F+1.0F));
    System.out.println();
    Maps.newHashMapWithExpectedSize(7);
  }
}    
   