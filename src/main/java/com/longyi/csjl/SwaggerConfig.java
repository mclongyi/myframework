package com.longyi.csjl;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
	
	@Bean
    public Docket swaggerSpringMvcPlugin() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(buildApiInf()).select().apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class)).build();
    }

    private ApiInfo buildApiInf(){
        return new ApiInfoBuilder()
                .title("业务中台--库存中心对内接口")
                .description("接口文档·openAPI")
                .build();
    }

}
