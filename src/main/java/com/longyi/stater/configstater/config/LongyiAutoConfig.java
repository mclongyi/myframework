package com.longyi.stater.configstater.config;

import com.longyi.stater.configstater.service.ExampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(LongyiConfigServer.class)
public class LongyiAutoConfig {

    @Autowired
    private LongyiConfigServer longyiConfigServer;
    @Bean
    /**
     * @ConditionalOnProperty 当yml 配置example.service.enabled=true 时，才会触发
     * 仅当 BeanFactory 中不包含指定的 bean class 和/或 name 时条件匹配
     */
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "mc.long.yi" , value = "enabled" ,havingValue = "true")
    public ExampleService exampleService(){
        return new ExampleService(longyiConfigServer.getName(),longyiConfigServer.getDesc());
    }

}
