package com.longyi.csjl.sjmo.prototype;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author ly
 * @description TODO
 * @date 2020/10/27 19:06
 * @throw
 */
@Data
@AllArgsConstructor
public class Person implements Cloneable{

    private String name;
    private String occupation;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Person person=null;
        try{
            person =(Person) super.clone();
        }catch (Exception e){

        }
        return person;
    }
}
   