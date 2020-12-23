package com.longyi.csjl.generic;

import com.longyi.csjl.unsafe.StudentDTO;

public class GentricTest {
  public static void main(String[] args) throws InstantiationException, IllegalAccessException {
      GeneraClass<String> generaClass =new GeneraClass<String>("saa");
      GeneraMethod generaMethod=new GeneraMethod();
      generaMethod.getnGenraMethod(StudentDTO.class);
       Character c='c';
  }
}
