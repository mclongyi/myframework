package com.longyi.stock.webservice;

import cn.hutool.http.webservice.SoapClient;

import javax.xml.soap.SOAPMessage;

/**
 * @author leiyi
 * @Description TODO
 * @date 2020/5/21
 */
public class SoapUtil {

    public void test(){
        SoapClient client = SoapClient.create("http://www.webxml.com.cn/WebServices/IpAddressSearchWebService.asmx")
                // 设置要请求的方法，此接口方法前缀为web，传入对应的命名空间
                .setMethod("web:getCountryCityByIp", "http://WebXml.com.cn/")
                // 设置参数，此处自动添加方法的前缀：web
                .setParam("theIpAddress", "218.21.240.106");
        String meg= client.send();
        System.out.println(meg);
    }
}    
   