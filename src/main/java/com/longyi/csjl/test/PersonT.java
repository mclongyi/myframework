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
public class PersonT{
    private Integer age;

    private String name;

}
   