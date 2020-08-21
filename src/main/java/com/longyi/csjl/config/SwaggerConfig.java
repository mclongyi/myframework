/**
 * Filename SwaggerConfig.java
 * Company 上海来伊份科技有限公司。
 * @author xly
 * @version 
 */
package com.longyi.csjl.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author xly
 * @since 2019年4月18日 下午12:11:51
 */
@Configuration
public class SwaggerConfig {

	@Bean
    public Docket swaggerSpringMvcPlugin() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(buildApiInf()).select().apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class)).build();
    }

    private ApiInfo buildApiInf(){
        return new ApiInfoBuilder()
                .title("业务中台--库存中心")
                .description("接口文档·库存核心服务")
                .build();
    }

}
