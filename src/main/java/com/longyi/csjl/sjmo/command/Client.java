package com.longyi.csjl.sjmo.command;

/**
 * @author ly
 * @description TODO
 * @date 2020/11/9 14:45
 * @throw
 */
public class Client {

    public static void main(String[] args) {
        Receiver receiver=new Receiver();
        ICommand command=new ConcreteCommand(receiver);
        Invoker invoker=new Invoker(command);
        invoker.call();
    }
}    
   