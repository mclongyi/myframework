package com.longyi.stock.design.patterns.command;

/**
 * @author ly
 * @Description TODO
 * @date 2020/5/26
 */
public class Client {

  public static void main(String[] args) {
    Receiver receiver=new Receiver();
    Command command=new ConcreteCommand(receiver);
    Invoker invoker=new Invoker(command);
    invoker.action();
  }
}    
   