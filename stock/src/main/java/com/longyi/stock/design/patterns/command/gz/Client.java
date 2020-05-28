package com.longyi.stock.design.patterns.command.gz;

import com.longyi.stock.design.patterns.command.Receiver;

import java.util.Arrays;

/**
 * @author ly
 * @Description TODO
 * @date 2020/5/27 22:33
 */
public class Client {

  public static void main(String[] args) {
      YKReceiver receiver=new YKReceiver();
      Command commandOn=new LightOnCommand(receiver);
      Command commandOff=new LightOffCommand(receiver);
      Invoker invoker=new Invoker(Arrays.asList(commandOn,commandOff));
      invoker.action();

  }
}    
   