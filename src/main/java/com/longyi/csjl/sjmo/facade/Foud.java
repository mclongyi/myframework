package com.longyi.csjl.sjmo.facade;

import java.math.BigDecimal;

/**
 * @author ly
 * @description 门面模式
 * @date 2020/11/1 15:38
 * @throw
 */
public class Foud {

    private Liquor liquor=new Liquor();

    private Wingtech wingtech=new Wingtech();

    public void buy(){
        liquor.buy(BigDecimal.ONE);
        wingtech.buy();
    }

    public void sale(){
        liquor.sale(BigDecimal.ONE);
        wingtech.sale();
    }

}
   