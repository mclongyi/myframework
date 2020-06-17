package com.longyi.configserver;

import com.longyi.configserver.config.LongyiService;
import com.longyi.configserver.service.MCLongyiSayService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author ly
 * @Description TODO
 * @date 2020/6/16 23:07
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class Test {

    @Autowired
    private LongyiService longyiService;

    @Autowired
    private MCLongyiSayService mcLongyiSayService;

    @org.junit.Test
    public void configTest(){
      System.out.println(longyiService.join());
    }

    @org.junit.Test
    public void serverTest(){
     mcLongyiSayService.sayHello();
    }
}    
   