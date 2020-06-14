package com.longyi.stock.rebuild.ifelse;

/**
 * @author ly
 * @Description TODO
 * @date 2020/6/14 17:46
 */
public interface OrderStencilledService {

    /***
     * 订单完结广播通知(1 - 支付完成)
     * @param orderBO
     */
    void dispatchModeFanout(OrderBO orderBO);

    /**
     * 创建出库单
     * @param orderBO
     */
    void createScmsDeliveryOrder(OrderBO orderBO);

}
