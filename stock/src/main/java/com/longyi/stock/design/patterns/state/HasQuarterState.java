package com.longyi.stock.design.patterns.state;

/**
 * @author ly
 * @Description 有25美分的状态业务对象
 * @date 2020/5/30 14:50
 */
public class HasQuarterState implements State {

    private GumballMachine gumballMachine;

    public HasQuarterState(GumballMachine gumballMachine){
        this.gumballMachine=gumballMachine;
    }

    @Override
    public void insertQuarter() {
        System.out.println("你已经投币成了..请勿重复投币");
    }

    @Override
    public void ejectQuarter() {
    System.out.println("退还25￥...");
    gumballMachine.setState(gumballMachine.getNoQuarterState());
    }

    @Override
    public void turnCrank() {
     System.out.println("系统正在操作，请稍等...");
     gumballMachine.setState(gumballMachine.getSoldState());
    }

    @Override
    public void dispense() {
    System.out.println("没有糖果分配...");
    }
}
   