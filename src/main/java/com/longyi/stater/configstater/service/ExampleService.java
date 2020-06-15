package com.longyi.stater.configstater.service;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExampleService {

    private String name;

    private String desc;

    public String join(){
        return this.name+this.desc;
    }
}

