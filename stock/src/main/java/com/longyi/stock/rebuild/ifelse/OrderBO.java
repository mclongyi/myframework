package com.longyi.stock.rebuild.ifelse;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ly
 * @Description TODO
 * @date 2020/6/14 16:10
 */
@Data
public class OrderBO {

    private Long orderId;

    private String orderNo;

    private BigDecimal payAmount;
}    
   