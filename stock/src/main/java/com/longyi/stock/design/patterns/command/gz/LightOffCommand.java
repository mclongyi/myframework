package com.longyi.stock.design.patterns.command.gz;

import com.longyi.stock.design.patterns.command.Receiver;

/**
 * @author ly
 * @Description TODO
 * @date 2020/5/27 22:16
 */
public class LightOffCommand implements Command {

    private YKReceiver receiver;
    public LightOffCommand(YKReceiver receiver){
        this.receiver=receiver;
    }

    @Override
    public void execute() {
        System.out.println("主人 主人 灯关了....");
        receiver.action();
    }
}
   