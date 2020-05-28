package com.longyi.stock.design.patterns.factory;

import org.junit.Test;

import javax.swing.*;

/**
 * @author leiyi
 * @Description TODO
 * @date 2020/5/24
 */
public class FactoryTest {

    @Test
    public void test(){
        AbstractFactory abstractFactory=new BMFactory();
        AbstractProduceCar car = abstractFactory.createCar();
        System.out.println(car.description());


        AbstractFactory abstractFactory1=new ADFactory();
        AbstractProduceCar car1 = abstractFactory1.createCar();
        System.out.println(car1.description());

    }
}    
   