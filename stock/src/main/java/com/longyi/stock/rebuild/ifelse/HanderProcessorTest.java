package com.longyi.stock.rebuild.ifelse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

/**
 * @author ly
 * @Description TODO
 * @date 2020/6/14 18:09
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HanderProcessorTest {

    @Autowired
    private HandlerContext HandlerContext;


    @Test
    public void sendMsg(){
        List<Integer> list= Arrays.asList(15,16);
         for(Integer type:list){
             AbstractHandler instance = HandlerContext.getInstance(type);
             OrderBO orderBO=new OrderBO();
             instance.handle(orderBO);
         }

    }
}    
   