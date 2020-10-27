package com.longyi.csjl.sjmo.prototype;

/**
 * @author ly
 * @description TODO
 * @date 2020/10/27 19:10
 * @throw
 */
public class PersonTest {

    public static void main(String[] args) throws CloneNotSupportedException {
        Person person=new Person("张三","测试");
        Person clone =(Person)person.clone();
        System.out.println(clone);
    }
}    
   