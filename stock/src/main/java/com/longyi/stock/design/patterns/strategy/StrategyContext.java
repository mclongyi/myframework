package com.longyi.stock.design.patterns.strategy;

/**
 * @author longyi
 * @Description context上下文
 * @date 2020/5/19
 */
public class StrategyContext {
    /**
     * 抽象策略对象
     */
    private AbstractStrategy strategy;

    public void setStrategy(AbstractStrategy strategy) {
        this.strategy = strategy;
    }

    public void sayHello() {
        strategy.sayHello();
    }


}
   