package com.longyi.stock.design.patterns.command;

/**
 * @author ly
 * @Description TODO
 * @date 2020/5/26
 */
public class Invoker {

    private Command command;

    public Invoker(Command command){
        this.command=command;
    }

    public void action(){
        command.execute();
    }

}
   