package com.longyi.stock.design.patterns.state;

import lombok.Data;

/**
 * @author ly
 * @Description 糖果状态机
 * @date 2020/5/30 14:37
 */
@Data
public class GumballMachine<toString> {
    State hasQuarterState;
    State noQuarterState;
    State soldOutState;
    State soldState;

    public GumballMachine(int numberGumballs) {
        hasQuarterState = new HasQuarterState(this);
        noQuarterState = new NoQuarterState(this);
        soldOutState = new SoldOutState(this);
        soldState = new SoldState(this);
        this.count = numberGumballs;
        if (numberGumballs > 0) {
            state = noQuarterState;
        }
    }

    /**
     * 初始化状态值
     */
    State state = soldOutState;
    /**
     * 初始数量值
     */
    int count = 0;


    public void insertQuarter() {
        state.insertQuarter();
    }

    public void ejectQuarter() {
        state.ejectQuarter();
    }

    public void turnCrank() {
        state.turnCrank();
    }

    public void dispense() {
        state.dispense();
    }

    void releaseBall() {
        System.out.println("一个糖果将要被售出...");
        if (count > 0) {
            count = count - 1;
        }
    }

    @Override
    public String toString() {

        return "当前状态:" + this.state.toString() + ":count:" + this.count;
    }

}    
   