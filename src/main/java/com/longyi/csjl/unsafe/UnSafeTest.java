package com.longyi.csjl.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * 基于Java unSafe类进行测试
 */
public class UnSafeTest {
  public static void main(String[] args) throws Exception {
      Field field = Unsafe.class.getDeclaredField("theUnsafe");
      field.setAccessible(true);
      Unsafe unsafeObject = (Unsafe) field.get(null);
      StudentDTO studentDTO=(StudentDTO)unsafeObject.allocateInstance(StudentDTO.class);
      studentDTO.setAddr("测试");
      studentDTO.setAge(20);
      studentDTO.setName("WBD");
      System.out.println(studentDTO.toString());
  }
}
