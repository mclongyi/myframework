package com.longyi.stock.design.patterns.decoration;

import org.junit.Test;

/**
 * @author leiyi
 * @Description TODO
 * @date 2020/5/22
 */
public class DecorationTest {


    @Test
    public void test(){
        Beverage beverage=new Espesso();
        System.out.println(beverage.getDescription()+" $"+beverage.cost());

        Beverage dareBeverage=new HouseBlend();
        System.out.println(dareBeverage.getDescription()+" $"+dareBeverage.cost());
        //添加调料
        dareBeverage=new Mocha(dareBeverage);
        dareBeverage=new Cream(dareBeverage);
        System.out.println(dareBeverage.getDescription()+" $"+dareBeverage.cost());
    }
}    
   