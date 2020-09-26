package com.longyi.csjl.jdks;

public class ReplaceTest {
  public static void main(String[] args) {
      String str="中国人民是很喜欢java C# java是一门艺术课程";
      String C = str.replace("java", "C++");
      String C2 = str.replaceFirst("java", "C++");
      String C3 = str.replaceAll("java", "111");
      System.out.println("replace:"+C);
      System.out.println("replaceFirst:"+C2);
      System.out.println("replaceAll:"+C3);
  }
}
