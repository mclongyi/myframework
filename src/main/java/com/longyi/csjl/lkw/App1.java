package com.longyi.csjl.lkw;

import io.swagger.models.auth.In;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class App1 {
    public static void main(String[] args){
//        Integer var1=new Integer(1);
//        Integer var2=var1;
//        doSomething(var2);
//        System.out.print(var1.intValue());
//        System.out.print(var1==var2);
        readAndWrite();

    }
    public static Integer doSomething(Integer integer){
        integer=new Integer(2);
        return integer;
    }

    public static void readAndWrite(){
        try {
            FileOutputStream fos = new FileOutputStream("C:/afile.txt");
            DataOutputStream dos = new DataOutputStream(fos);
            dos.writeInt(3);
            dos.writeChar(1);
            dos.close();
            fos.close();
        } catch (IOException e) {}

    }
}
