package com.longyi.csjl.value;

import java.util.Random;

public class StudentService {

    public Student buildStudent(){
        Student student=new Student();
        student.setAge(22);
        student.setName("小样子");
        return student;
    }

    public Student buildStudent(Student studentRet){
        Student student=new Student();
        student.setAge(22);
        student.setName("小样子");
        studentRet=student;
        return studentRet;
    }

    public ClassInfo buildClassInfo(){
        ClassInfo classInfo=new ClassInfo();
        classInfo.setClassId(new Random().nextLong());
        classInfo.setClassName("六年级");
        classInfo.setGradeName("三班");
        return classInfo;
    }
    public ClassInfo buildClassInfo(ClassInfo classInfoRet){
        ClassInfo classInfo=new ClassInfo();
        classInfo.setClassId(new Random().nextLong());
        classInfo.setClassName("六年级");
        classInfo.setGradeName("三班");
        classInfoRet=classInfo;
        return classInfoRet;
    }

    public void buildInfo(Student student,ClassInfo classInfo){
        student=this.buildStudent();
        classInfo=this.buildClassInfo();
    }

}
