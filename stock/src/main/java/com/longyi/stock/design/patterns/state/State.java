package com.longyi.stock.design.patterns.state;

/**
 * 系统中的状态
 *
 * @author Lenovo
 */
public interface State {

    /**
     * 投币25美分
     */
    void insertQuarter();

    /**
     * 退出25美分
     */
    void ejectQuarter();

    /**
     * 转动手柄
     */
    void turnCrank();

    /**
     * 分配
     */
    void dispense();


}
