package com.longyi.csjl.aop;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mclongyi")
@Api(tags={"测试接口"})
public class AopLogController {



    @ApiOperation(value = "查询写日志喜喜", nickname = "queryStudentInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success", response = String.class)
    @LoggerCallAnnotation(recordCode = "#recordInfo.recordCode")
    @PostMapping("/writeLog")
    public String queryStudentInfo(@RequestBody RecordInfo recordInfo){
     System.out.println(recordInfo.toString());
     return JSON.toJSONString(recordInfo);
    }

}
