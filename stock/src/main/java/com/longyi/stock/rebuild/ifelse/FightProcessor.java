package com.longyi.stock.rebuild.ifelse;

import org.springframework.stereotype.Component;

/**
 * @author ly
 * @Description TODO
 * @date 2020/6/14 18:02
 */
@Component
@OrderHandlerType(15)
public class FightProcessor extends AbstractHandler {
    @Override
    public void handle(OrderBO orderBO) {
        System.out.println("开始处理社区团业务");
    }
}
   