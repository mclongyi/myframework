package com.longyi.stock.design.patterns.command.lj;

/**
 * @author ly
 * @Description TODO
 * @date 2020/5/27
 */
public class LightOffCommand implements Command {

    private Light light;

    private Integer preStatus;

    public LightOffCommand(Light light){
        this.light=light;
    }

    @Override
    public void execute() {
        preStatus=light.getStatus();
        light.off();

    }

    @Override
    public void undo() {
        if(Light.ON.equals(preStatus)){
            light.lightOn();
        } else {
            light.off();
        }
    }
}
   