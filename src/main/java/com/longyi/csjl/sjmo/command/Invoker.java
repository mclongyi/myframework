package com.longyi.csjl.sjmo.command;

/**
 * @author ly
 * @description TODO
 * @date 2020/11/9 10:13
 * @throw
 */
public class Invoker {
   private ICommand command;

   public Invoker(ICommand command){
       this.command=command;
   }
   public  void call(){
       command.execute();
   }

}
   