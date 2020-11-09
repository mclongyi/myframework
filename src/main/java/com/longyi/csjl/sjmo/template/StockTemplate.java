package com.longyi.csjl.sjmo.template;

/**
 * @author ly
 * @description TODO
 * @date 2020/11/5 18:02
 * @throw
 */
public class StockTemplate extends TemplateAbstract {
    @Override
    public void saveBefore() {
        System.out.println("对对象进行校验");
    }

    @Override
    public void saveAfter() {
        System.out.println("保存数据成功 通知其他中心");
    }

    @Override
    public void doSave() {
        System.out.println("正在保存数据。。。");
        System.out.println("正在保存明细数据。。。");
    }
}
   