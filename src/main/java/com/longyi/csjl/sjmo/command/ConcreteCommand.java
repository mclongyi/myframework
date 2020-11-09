package com.longyi.csjl.sjmo.command;

/**
 * @author ly
 * @description TODO
 * @date 2020/11/9 10:10
 * @throw
 */
public class ConcreteCommand implements ICommand {
    private Receiver receiver;

    public ConcreteCommand(Receiver receiver){
        this.receiver=receiver;
    }

    @Override
    public void execute() {
        receiver.action();
    }
}
   