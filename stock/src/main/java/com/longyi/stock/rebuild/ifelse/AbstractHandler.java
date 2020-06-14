package com.longyi.stock.rebuild.ifelse;

/**
 * @author ly
 * @Description TODO
 * @date 2020/6/14 16:10
 */
public abstract class AbstractHandler {

    /**
     * 抽象业务处理类
     * @param orderBO
     */
    abstract public void handle(OrderBO orderBO);
}    
   