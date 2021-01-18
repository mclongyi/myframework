package com.longyi.csjl;

import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@RetrofitScan("com.longyi.csjl.retrofitscan")
@MapperScan(basePackages={"com.longyi.csjl.domain.resporitory","com.longyi.csjl.tools"})
@EnableSwagger2
public class CsjlApplication {

    public static void main(String[] args) {
        SpringApplication.run(CsjlApplication.class, args);
    }

}
