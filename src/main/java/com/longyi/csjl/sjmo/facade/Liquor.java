package com.longyi.csjl.sjmo.facade;

import java.math.BigDecimal;

/**
 * @author ly
 * @description TODO
 * @date 2020/11/1 15:39
 * @throw
 */
public class Liquor {

    public void buy(BigDecimal value){
        System.out.println("买入白酒基金:"+value);
        System.out.println("专业投资人员对资金进行分配操作");
    }

    public void  sale(BigDecimal value){
        System.out.println("卖出白酒基金:"+value);
        System.out.println("专业投资人员对资金进行分配操作");
    }

}    
   