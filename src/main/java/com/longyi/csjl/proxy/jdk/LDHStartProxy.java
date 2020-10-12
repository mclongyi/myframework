package com.longyi.csjl.proxy.jdk;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author ly
 * @Description TODO
 * @date 2020/7/14 9:35
 */
public interface LDHStartProxy {

    /**
     * 讨论演出名称和出场费
     * @param showName
     * @param showMoney
     */
    void dissShow(String showName, BigDecimal showMoney);

    /**
     * 按时计费
     * @param startTime
     * @return
     */
    List<String> showInfo(Date startTime);

}
