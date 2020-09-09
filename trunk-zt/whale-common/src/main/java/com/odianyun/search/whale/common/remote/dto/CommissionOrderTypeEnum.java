package com.odianyun.search.whale.common.remote.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hs
 * @date 2018/9/4.
 */

public enum CommissionOrderTypeEnum {
    distributor(1, "消费商分佣"),
    group(2, "拼团分佣"),
    coupon(3, "优惠券分佣"),
    share(4, "分享分佣");

    private String desc;
    private Integer code;

    private CommissionOrderTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static List<Integer> getCodes() {
        List<Integer> list = new ArrayList();
        CommissionOrderTypeEnum[] arr$ = values();
        int len$ = arr$.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            CommissionOrderTypeEnum cs = arr$[i$];
            list.add(cs.code);
        }

        return list;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return this.desc;
    }

    public static CommissionOrderTypeEnum enumOf(Integer ordinal) {
        CommissionOrderTypeEnum[] arr$ = values();
        int len$ = arr$.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            CommissionOrderTypeEnum cs = arr$[i$];
            if (ordinal == cs.ordinal()) {
                return cs;
            }
        }

        return null;
    }

    @JsonCreator
    public static CommissionOrderTypeEnum getEnum(Integer number) {
        CommissionOrderTypeEnum[] arr$ = values();
        int len$ = arr$.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            CommissionOrderTypeEnum plat = arr$[i$];
            if (plat.getCode().equals(number)) {
                return plat;
            }
        }

        return null;
    }

    @JsonValue
    public Integer getCode() {
        return this.code;
    }

    public static String getDesc(Integer number) {
        if (number == null) {
            return null;
        } else {
            CommissionOrderTypeEnum[] arr$ = values();
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                CommissionOrderTypeEnum plat = arr$[i$];
                if (plat.getCode().equals(number)) {
                    return plat.getDesc();
                }
            }

            return "未定义";
        }
    }
}