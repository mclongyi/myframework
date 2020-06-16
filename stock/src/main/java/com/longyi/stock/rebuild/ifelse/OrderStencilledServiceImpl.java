package com.longyi.stock.rebuild.ifelse;

import org.springframework.stereotype.Service;

/**
 * @author ly
 * @Description TODO
 * @date 2020/6/14 17:50
 */
@Service
public class OrderStencilledServiceImpl implements OrderStencilledService {
    @Override
    public void dispatchModeFanout(OrderBO orderBO) {
        System.out.println("订单处理完成 开发派发...");
    }

    @Override
    public void createScmsDeliveryOrder(OrderBO orderBO) {
        System.out.println("开始创建出库单...");
    }
}
   