package com.ly.springdatajap.config;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@ComponentScan("ccom.ly.springdatajap.controller")
@EnableSwagger2
public class SwaggerConfig {


    @Bean
    public Docket accountApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("测试JPA")
                .apiInfo(apiInfo())
                .select()
                .paths(accountPaths())
                .build();
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("公文流转模块")
                .description("<h4>接口里pager对象只在查询列表时用到</h4>")
                .termsOfServiceUrl("http://springfox.io")
                .contact("333")
                .license("Apache License Version 2.0")
                .licenseUrl("https://github.com/springfox/springfox/blob/master/LICENSE")
                .version("1.0")
                .build();
    }

    private Predicate<String> accountPaths() {
        return or(
                regex("/jpa/.*")
        );
    }


}
