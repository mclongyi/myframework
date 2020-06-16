package com.longyi.stock.design.patterns.command.lj;

/**
 * @author ly
 * @Description TODO
 * @date 2020/5/27
 */
public class RomeController {

    private Command[] onCommands;

    private Command[] offCommands;

    private Command undoCommand;

    private Command lastCommand;

    public RomeController() {
        NoCommand noCommand = new NoCommand();
        onCommands = new Command[3];
        offCommands = new Command[3];
        for (int i = 0; i < 3; i++) {
            onCommands[i] = noCommand;
            offCommands[i] = noCommand;
        }
        undoCommand = noCommand;
    }


    public void setCommand(int position, Command onCommand, Command offCommand) {
        onCommands[position] = onCommand;
        offCommands[position] = offCommand;
    }

    public void onButtonPushed(int position) {
        this.lastCommand = onCommands[position];
        onCommands[position].execute();
    }

    /**
     * 选择按第几号off按钮
     *
     * @param position
     */
    public void offButtonPushed(int position) {
        this.lastCommand = offCommands[position];
        offCommands[position].execute();
    }

    /**
     * 撤销最后一次执行的命令
     */
    public void undo() {
        lastCommand.undo();
    }


}
   