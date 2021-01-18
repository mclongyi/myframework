package com.longyi.csjl.source.code;

import java.util.HashMap;

/**
 * @author ly
 * @description TODO
 * @date 2021/1/18 9:27
 * @throw
 */
public class AppTest {

    public static void main(String[] args) {
        MyHashMap<String,String> hashMap=new MyHashMap();
        hashMap.put("aa","aaa");
        hashMap.put("bb","222");
        hashMap.put("bb","cccc");
        HashMap hashMap1=new HashMap();
        hashMap1.put("1",2);
        System.out.println(hashMap.get("aa")+" "+hashMap.get("bb"));
    }
}    
   