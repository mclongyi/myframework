package com.longyi.stock.rebuild.ifelse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author ly
 * @Description TODO
 * @date 2020/6/14 17:36
 */
@Component
public class HandlerContext {

    @Autowired
    private ApplicationContext beanFactory;

    public  AbstractHandler getInstance(Integer type){

        Map<Integer,Class> map = (Map<Integer, Class>) beanFactory.getBean(OrderHandlerType.class.getName());

        return (AbstractHandler)beanFactory.getBean(map.get(type));
    }
}    
   