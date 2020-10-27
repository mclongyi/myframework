package com.longyi.csjl.js;

/**
 * @author ly
 * @description TODO
 * @date 2020/10/21 16:11
 * @throw
 */
public class BitOperation {


    public int add(int num1,int num2){
        if(num2 == 0){
            return num1;
        }
        int sum=num1^num2;
        int carry=(num1&num2)<<1;
        return add(sum,carry);
    }

}    
   