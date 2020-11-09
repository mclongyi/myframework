package com.longyi.csjl.sjmo.template;

/**
 * @author ly
 * @description TODO
 * @date 2020/11/5 18:08
 * @throw
 */
public class TradeTemplate extends TemplateAbstract {
    @Override
    public void saveBefore() {
        System.out.println("交易正在进行前置保存...");
    }

    @Override
    public void saveAfter() {
        System.out.println("交易保存成功...");
    }

    @Override
    public void doSave() {
        System.out.println("正在进行业务保存...");
    }
}
   