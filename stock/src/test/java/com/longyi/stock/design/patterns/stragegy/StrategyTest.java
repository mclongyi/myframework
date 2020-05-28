package com.longyi.stock.design.patterns.stragegy;

import com.longyi.stock.design.patterns.strategy.AbstractStrategy;
import com.longyi.stock.design.patterns.strategy.ChinaStrategy;
import com.longyi.stock.design.patterns.strategy.StrategyContext;
import com.longyi.stock.design.patterns.strategy.UsaStrategy;
import org.junit.Test;

/**
 * @author longyi
 * @Description TODO
 * @date 2020/5/19
 */
public class StrategyTest {



    @Test
    public void testStrategy(){
        StrategyContext strategyContext=new StrategyContext();
        //chinese people
        AbstractStrategy chinaStrategy=new ChinaStrategy();
        strategyContext.setStrategy(chinaStrategy);
        chinaStrategy.sayHello();
        System.out.println("=============");
        //usa people
        AbstractStrategy usaStrategy=new UsaStrategy();
        strategyContext.setStrategy(usaStrategy);
        usaStrategy.sayHello();
    }


}
   