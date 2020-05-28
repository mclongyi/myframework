package com.longyi.stock.design.patterns.command.gz;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ly
 * @Description TODO
 * @date 2020/5/27 22:21
 */
public class Invoker {

    private List<Command> list=new ArrayList<>();

    public Invoker(List<Command> commands){
        list.addAll(commands);
    }

    public void action(){
      list.forEach(x->x.execute());
    }

}
   