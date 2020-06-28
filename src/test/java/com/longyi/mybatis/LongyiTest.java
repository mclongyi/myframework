package com.longyi.mybatis;

import org.junit.jupiter.api.Test;

/**
 * @author ly
 * @Description TODO
 * @date 2020/6/24 17:02
 */
public class LongyiTest {

    @Test
    public void test(){
        int test=20;
        System.out.println(test>>2);
        System.out.println(test>>>2);

        int res=-20;
        System.out.println(res>>2);
        System.out.println(res>>>2);
    }

}    
   