package com.longyi.stock.rebuild.ifelse;


import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


/**
 * @author ly
 * @Description TODO
 * @date 2020/6/14 16:16
 */
@Component
@SuppressWarnings({"unused", "rawtypes"})
public class HandlerProcessor implements BeanFactoryPostProcessor {

    private String basePackage = "com.longyi.stock.rebuild.ifelse";

    public static final Logger log = LoggerFactory.getLogger(HandlerProcessor.class);

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        Map<Integer, Class> map = new HashMap<Integer, Class>();

        ClassScanner.scan(basePackage, OrderHandlerType.class).forEach(x -> {
            int type = x.getAnnotation(OrderHandlerType.class).value();
            map.put(type, x);
        });

        beanFactory.registerSingleton(OrderHandlerType.class.getName(), map);

        log.info("处理器初始化{}", JSONObject.toJSONString(beanFactory.getBean(OrderHandlerType.class.getName())));
    }
}
   