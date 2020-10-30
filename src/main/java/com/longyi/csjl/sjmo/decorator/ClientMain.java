package com.longyi.csjl.sjmo.decorator;

/**
 * @author ly
 * @description TODO
 * @date 2020/10/30 16:52
 * @throw
 */
public class ClientMain {

    public static void main(String[] args) {
        BeverageAbstractComponent component=new MilkTeam();
        //添加咖啡的奶茶
        CondimentAbstractDecorator decorator=new Coffee(component);
        System.out.println("费用:"+decorator.cost()+" "+decorator.getDesc());
        //添加咖啡 绿茶的奶茶
        decorator=new Team(decorator);
        System.out.println("费用:"+decorator.cost()+" "+decorator.getDesc());


    }
}    
   