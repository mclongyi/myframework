package com.longyi.csjl.proxy.jdk;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ly
 * @Description TODO
 * @date 2020/7/14 10:09
 */
public class LDHProxyService implements LDHStartProxy {
    @Override
    public void dissShow(String showName, BigDecimal showMoney) {
       System.out.println("演唱会名称:"+showName+",出场费:"+showMoney);
    }

    @Override
    public List<String> showInfo(Date startTime) {
        List list=new ArrayList(){{
            add("aaa");
            add("vvv");
        }};
        return list;
    }
}
   