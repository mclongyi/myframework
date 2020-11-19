package com.longyi.csjl.future;

import com.longyi.csjl.value.Student;

import java.util.concurrent.Callable;

/**
 * @author Administrator
 */
public class FutureTaskService implements Callable<Student> {
    private String threadName;

    public  FutureTaskService(String threadName){
        this.threadName=threadName;
    }

    @Override
    public Student call() throws Exception {
    System.out.println("正在进行远程rpc操作...");
    System.out.println("正在连接远程服务器，请等待2s");
    System.out.println("远程服务器返回...");
    Student student=new Student();
    student.setName("小明");
    student.setAge(10);
    return student;
    }
}
