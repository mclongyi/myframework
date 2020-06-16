package com.longyi.stock.design.patterns.state;

/**
 * @author ly
 * @Description TODO
 * @date 2020/5/30 15:42
 */
public class Client {

    public static void main(String[] args) {
        GumballMachine gumballMachine = new GumballMachine(3);
        System.out.println(gumballMachine.toString());

        gumballMachine.insertQuarter();
        gumballMachine.turnCrank();
        gumballMachine.dispense();
        System.out.println(gumballMachine.toString());


        gumballMachine.insertQuarter();
        gumballMachine.turnCrank();
        gumballMachine.dispense();
        System.out.println(gumballMachine.toString());

        gumballMachine.insertQuarter();
        gumballMachine.turnCrank();
        gumballMachine.dispense();
        System.out.println(gumballMachine.toString());


        gumballMachine.insertQuarter();
        gumballMachine.turnCrank();
        gumballMachine.dispense();
        System.out.println(gumballMachine.toString());
    }
}    
   