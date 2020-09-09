package com.odianyun.search.whale.api.model.req;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ClientRequestInfo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    //客户端自设名
    private String poolName;

    private String IP;

    //设备型号
    private String model;

    //操作系统
    private String os;

    //设备号
    private String deviceId;

    //app版本
    private String appVersion;


    //浏览器版本
    private String browserVersion;

}
