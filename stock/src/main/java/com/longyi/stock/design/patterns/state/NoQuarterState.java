package com.longyi.stock.design.patterns.state;

/**
 * @author ly
 * @Description 没有25美分的状态业务对象
 * @date 2020/5/30 14:49
 */
public class NoQuarterState implements State{

    private GumballMachine gumballMachine;

    public NoQuarterState(GumballMachine gumballMachine){
        this.gumballMachine=gumballMachine;
    }

    @Override
    public void insertQuarter() {
        System.out.println("投币成功:25$");
        gumballMachine.setState(gumballMachine.getHasQuarterState());
    }

    @Override
    public void ejectQuarter() {
    System.out.println("你未投币...");
    }

    @Override
    public void turnCrank() {
    System.out.println("你转动了手柄 但未投币");
    }

    @Override
    public void dispense() {
    System.out.println("请先投币...");
    }
}
   