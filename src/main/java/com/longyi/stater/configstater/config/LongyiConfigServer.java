package com.longyi.stater.configstater.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@AllArgsConstructor
@ConfigurationProperties("mc.long.yi")
public class LongyiConfigServer {

    private String name;

    private String desc;


}
