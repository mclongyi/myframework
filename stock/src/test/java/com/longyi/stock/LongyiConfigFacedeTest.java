package com.longyi.stock.stater;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestConfig {

    @Autowired
    private LongyiConfig longyiConfig;

    @Test
    public void say(){
        longyiConfig.sayName();
    }
}
