package com.longyi.configserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ly
 * @Description TODO
 * @date 2020/6/16 22:58
 */
@Configuration
@EnableConfigurationProperties(LongyiConfigServer.class)
public class LongyiAutoConfig {
    @Autowired
    private LongyiConfigServer longyiConfigServer;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "mc.long.yi",value = "enable",havingValue = "true")
    public LongyiService longyiService(){
        return new LongyiService(longyiConfigServer.getName(),longyiConfigServer.getDesc());
    }
}
   