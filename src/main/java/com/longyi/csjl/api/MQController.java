package com.longyi.csjl.api;

import com.longyi.csjl.elasticsearch.Student;
import com.longyi.csjl.mq.rocket.StudentProducter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags={"MQ API接口"})
public class MQController {

    @Autowired
    private StudentProducter studentProducter;

    @ApiOperation(value = "发送消息", nickname = "sendMsg", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @PutMapping("/sendMsg")
    public String sendMsg(@RequestBody Student student){
        studentProducter.sendMsg(student);
        return "200";
    }

}
