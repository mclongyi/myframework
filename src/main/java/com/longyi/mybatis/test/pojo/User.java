package com.longyi.mybatis.test.pojo;

import lombok.Data;

/**
 * @author ly
 * @Description TODO
 * @date 2020/6/18 15:01
 */
@Data
public class User {

    private Long id;

    private String name;

    private String addr;

    private Integer age;

    private Integer sex;

    private String password;

    @Override
    public String toString(){
        return this.id+":"+this.name+":"+this.addr+":"+this.age+":"+this.sex+":"+this.password;
    }

}    
   