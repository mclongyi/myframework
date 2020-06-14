package com.longyi.stock.design.patterns.state;

/**
 * @author ly
 * @Description 糖果可售状态的业务对象
 * @date 2020/5/30 14:47
 */
public class SoldState implements State {

    private GumballMachine gumballMachine;

    public SoldState(GumballMachine gumballMachine){
        this.gumballMachine=gumballMachine;
    }

    @Override
    public void insertQuarter() {
        System.out.println("稍等 糖果正在出库中...");
    }

    @Override
    public void ejectQuarter() {
        System.out.println("你已经转动手柄，不能进行退款操作...");
    }

    @Override
    public void turnCrank() {
        System.out.println("系统正在处理中,请勿重复操作...");
    }

    @Override
    public void dispense() {
        gumballMachine.releaseBall();
        if(gumballMachine.getCount()>0){
            gumballMachine.setState(gumballMachine.getNoQuarterState());
        }else{
            gumballMachine.setState(gumballMachine.getSoldOutState());
        }
    }
}
   