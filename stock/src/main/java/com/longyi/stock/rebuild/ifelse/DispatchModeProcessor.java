package com.longyi.stock.rebuild.ifelse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ly
 * @Description TODO
 * @date 2020/6/14 15:54
 */
@Component
@OrderHandlerType(16)
public class DispatchModeProcessor extends AbstractHandler {

    @Autowired
    private OrderStencilledService orderStencilledService;

    @Override
    public void handle(OrderBO orderBO) {

        /**
         * 订单完结广播通知(1 - 支付完成)
         */
        orderStencilledService.dispatchModeFanout(orderBO);

        /**
         *  SCMS 出库单
         */
        orderStencilledService.createScmsDeliveryOrder(orderBO);
    }
}    
   