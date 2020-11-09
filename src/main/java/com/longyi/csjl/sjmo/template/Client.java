package com.longyi.csjl.sjmo.template;

/**
 * @author ly
 * @description TODO
 * @date 2020/11/5 18:10
 * @throw
 */
public class Client {

    public static void main(String[] args) {
        TemplateAbstract templateAbstract=new StockTemplate();
        templateAbstract.doSave();
    }
}    
   