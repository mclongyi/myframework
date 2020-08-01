package com.longyi.csjl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@ComponentScan(basePackages ={"com.longyi.csjl.aop"})
public class CsjlApplication {

    public static void main(String[] args) {
        SpringApplication.run(CsjlApplication.class, args);
    }

}
