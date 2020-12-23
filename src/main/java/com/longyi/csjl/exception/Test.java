package com.longyi.csjl.exception;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static int x=1;
    public static int y=10;
    public static int z=100;
  public static void main(String[] args) {
      finallyReturn();
      System.out.println("x="+x+" y="+y+" z="+z);
      List list=new ArrayList<>();
  }

  public static int finallyNotWork(){
      int temp=10000;
      try{
          throw new Exception();
      }catch (Exception e){
          return ++temp;
      }finally{
            temp=99999;
      }
  }

  public static int finallyReturn(){
      try{
         return ++x;
      }catch (Exception e){
          return ++y;
      }finally{
         return ++z;
      }
  }
}
