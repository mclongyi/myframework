package com.longyi.csjl.sjmo.template;

/**
 * @author ly
 * @description TODO
 * @date 2020/11/5 17:55
 * @throw
 */
public abstract class TemplateAbstract {

    /**
     * 核心流水线业务
     */
    public void assemblyLine(){
        saveBefore();
        doSave();
        saveAfter();
    }

    /**
     *  保存前置条件
     */
    public abstract void saveBefore();


    /**
     * 保存后置
     */
    public abstract void saveAfter();


    /**
     * 保存核心业务
     */
    public abstract void doSave();

}    
   