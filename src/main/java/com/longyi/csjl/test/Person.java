package com.longyi.csjl.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author ly
 * @Description TODO
 * @date 2020/8/14 16:06
 */
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@ToString
public class Person implements Comparable<Person>{

    private Integer age;

    private String name;

    @Override
    public int compareTo(Person o) {
        if(o.getAge()<this.getAge()){
            return 1;
        }else if(this.age<o.getAge()){
            return -1;
        }else{
            return this.name.compareTo(o.getName());
        }
    }
}
   