package com.longyi.stock;

import com.longyi.configserver.service.MCLongyiSayService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author ly
 * @Description TODO
 * @date 2020/6/17 9:24
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LongyiConfigFacedeTest {
    @Autowired
    private MCLongyiSayService mcLongyiSayService;

    @Test
    public void longyiTest(){
        mcLongyiSayService.sayHello();
    }


}    
   