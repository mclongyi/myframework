package com.longyi.stock.design.patterns.command;

/**
 * @author ly
 * @Description TODO
 * @date 2020/5/26
 */
public class ConcreteCommand implements Command {

    private Receiver receiver;

    public ConcreteCommand(Receiver receiver){
        this.receiver=receiver;
    }

    @Override
    public void execute() {
        receiver.action();
    }
}
   