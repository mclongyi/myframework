package com.longyi.csjl.api;

import com.github.pagehelper.PageInfo;
import com.longyi.csjl.elasticsearch.Student;
import com.longyi.csjl.elasticsearch.StudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags={"elasticSearch API接口"})
public class StudentController {
    @Autowired
    private StudentService studentService;

    @ApiOperation(value = "新增学生信息", nickname = "addStudent", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @PostMapping("/addStudent")
    public String addStudent(@RequestBody Student student){
        studentService.save(student);
        return "success";
    }


    @ApiOperation(value = "删除所有信息", nickname = "deleteAll", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @DeleteMapping("/deleteAll")
    public String deleteAll(){
        studentService.delAll();
        return "200";
    }

    @ApiOperation(value = "根据条件查询", nickname = "queryEsInfoByCondition", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success",response =Student.class)
    @PostMapping("/queryEsInfoByCondition")
    public PageInfo<Student> queryEsInfoByCondition(@RequestBody Student student){
        return studentService.queryEsInfoByCondition(student);
    }
}
