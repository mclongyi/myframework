package com.longyi.stock.design.patterns.state;

/**
 * @author ly
 * @Description 糖果售罄的业务对象
 * @date 2020/5/30 14:49
 */
public class SoldOutState implements State {

    private GumballMachine gumballMachine;

    public SoldOutState(GumballMachine gumballMachine) {
        this.gumballMachine = gumballMachine;
    }

    @Override
    public void insertQuarter() {
        System.out.println("已经售罄...");
    }

    @Override
    public void ejectQuarter() {
        System.out.println("已经售罄...");
    }

    @Override
    public void turnCrank() {
        System.out.println("已经售罄...");
    }

    @Override
    public void dispense() {
        System.out.println("已经售罄...");
    }
}
   