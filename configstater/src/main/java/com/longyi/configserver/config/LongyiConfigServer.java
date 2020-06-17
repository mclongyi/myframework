package com.longyi.configserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ly
 * @Description TODO
 * @date 2020/6/16 22:47
 */


@Data
@ConfigurationProperties("mc.long.yi")
public class LongyiConfigServer {
    private String name;

    private String desc;

    public LongyiConfigServer(String name,String desc){
        this.name=name;
        this.desc=desc;
    }
    public LongyiConfigServer(){}

}    
   