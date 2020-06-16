package com.longyi.stock.design.patterns.command.lj;

/**
 * @author ly
 * @Description TODO
 * @date 2020/5/27
 */
public class Light {
    public static final Integer ON = 1;
    private static final Integer OFF = 0;
    private Integer status = OFF;

    public void lightOn() {
        this.status = ON;
        System.out.println("the light is on...");
    }

    public void off() {
        this.status = OFF;
        System.out.println("the light is off...");
    }

    public Integer getStatus() {
        return this.status;
    }


}
   