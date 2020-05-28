package com.longyi.stock.design.patterns.command;

import com.longyi.stock.design.patterns.command.lj.Command;
import com.longyi.stock.design.patterns.command.lj.Light;
import com.longyi.stock.design.patterns.command.lj.LightOffCommand;
import com.longyi.stock.design.patterns.command.lj.LightOnCommand;
import com.longyi.stock.design.patterns.command.lj.RomeController;

/**
 * @author ly
 * @Description TODO
 * @date 2020/5/27
 */
public class RometControllTest {


    public static void main(String[] args) {

        Light livingRoomLight = new Light();
        Command lightOnCommand = new LightOnCommand(livingRoomLight);
        Command lightOffCommand = new LightOffCommand(livingRoomLight);
        RomeController remoteControl = new RomeController();
        remoteControl.setCommand(0,lightOnCommand,lightOffCommand);
        remoteControl.onButtonPushed(0);        //关上电灯
        remoteControl.undo();
        remoteControl.onButtonPushed(0);
        remoteControl.offButtonPushed(0);        //按下撤销键，再次开启电灯
        remoteControl.undo();        //打开空调
    }

}    
   