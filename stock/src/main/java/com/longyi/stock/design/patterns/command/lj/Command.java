package com.longyi.stock.design.patterns.command.lj;

public interface Command {
    /**
     * 执行方法
     */
     void execute();

    /**
     * 撤销方法
     */
    void undo();
}
