package com.longyi.csjl.value;

import com.alibaba.fastjson.JSON;

public class ReferenceTest {

  public static void main(String[] args) {
    Student student=new Student();
    ClassInfo classInfo=new ClassInfo();
    StudentService studentService=new StudentService();
    Student student1 = studentService.buildStudent(student);
    ClassInfo classInfo1 = studentService.buildClassInfo(classInfo);

    System.out.println(student1);
    System.out.println(classInfo1);
    studentService.buildInfo(student,classInfo);
    System.out.println(student + ":" + JSON.toJSONString(student));
    System.out.println(classInfo+":"+JSON.toJSONString(classInfo));
  }




}
