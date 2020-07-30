package com.longyi.csjl.builder;

public class SqlBuilderTest {
  public static void main(String[] args) {
      SqlBuilder build = new SqlBuilder.Builder().buildUserName("33333")
              .buildPassword("3333")
              .buildUrl("http://localhost:3360/")
              .build();
    System.out.println(build.toString());
  }
}
