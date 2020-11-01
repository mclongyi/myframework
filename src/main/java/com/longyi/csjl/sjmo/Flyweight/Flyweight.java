package com.longyi.csjl.sjmo.Flyweight;

import lombok.Data;

/**
 * @author ly
 * @description 抽象享元角色
 * @date 2020/11/1 16:36
 * @throw
 */
@Data
public abstract class Flyweight {

    /**
     * 内部状态
     */
    public String intrinsic;

    /**
     * 外部状态
     */
    public final String extrinsic;

    public Flyweight(String extrinsic){
        this.extrinsic=extrinsic;
    }

    /**
     * 定义操作
     * @param extrinsic
     */
    public abstract void operate(int extrinsic);



}
   