package com.longyi.stock.stater;

import com.longyi.stater.configstater.service.ExampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LongyiConfig {

    @Autowired
    private ExampleService exampleService;

    public void sayName(){
        String join = exampleService.join();
        System.out.println(join);
    }
}
