package com.longyi.csjl.sjmo.builder;

/**
 * @author ly
 * @description TODO
 * @date 2020/10/26 22:51
 * @throw
 */
public class BuilderTest {

    public static void main(String[] args) {
        Computer build = new Computer.Builder("因特尔", "三星")
                .setDisplay("艾默生")
                .setKeyboard("话说")
                .setUsbCount(30)
                .build();


    }
}    
   