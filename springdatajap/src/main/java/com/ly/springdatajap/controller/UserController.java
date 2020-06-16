package com.ly.springdatajap.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.ly.springdatajap.entity.Student;
import com.ly.springdatajap.entity.UserInfo;
import com.ly.springdatajap.service.StudentService;
import com.ly.springdatajap.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Api(description = "JAP测试")
@RestController
@RequestMapping("jpa")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private StudentService studentService;

    @PostMapping("/addUser")
    @ApiOperation(value = "保存用户", notes = "返回成功或者失败")
    public String addUser(@RequestBody UserInfo userInfo) {
        if (Objects.nonNull(userInfo)) {
            userInfo.setCreateDate(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            boolean b = userService.addUser(userInfo);
            if (b) {
                return "200OK";
            }
        }
        return "fail";
    }


    @PostMapping("/delUser/{id}")
    @ApiOperation(value = "保存用户", notes = "返回成功或者失败")
    public String delUser(@PathVariable("id") Integer id) {
        if (Objects.nonNull(id)) {
            boolean b = userService.delete(id);
            if (b) {
                return "200K";
            }
        }
        return "fail";
    }


    @PostMapping("/update")
    @ApiOperation(value = "保存用户", notes = "返回成功或者失败")
    public String update(@RequestBody UserInfo userInfo) {
        if (Objects.nonNull(userInfo)) {
            Optional<UserInfo> byId = userService.findById(userInfo.getId());
            UserInfo o = byId.map(x -> {
                x.setRoleInfoList(userInfo.getRoleInfoList());
                x.setAge(userInfo.getAge());
                x.setName(userInfo.getName());
                x.setCreateDate(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                return x;
            }).orElse(null);
            if (Objects.nonNull(o)) {
                userService.addUser(o);
            }
            return "200K";
        }
        return "fail";
    }

    @PostMapping("/findByConsidition")
    @ApiOperation(value = "获取用户列表", notes = "返回成功或者失败")
    public Page<UserInfo> findByConsidition(@RequestParam(value = "searchKey", required = false) String searchKey,
                                            @RequestParam(value = "beginDate", required = false) String beginDate,
                                            @RequestParam(value = "endDate", required = false) String endDate, @RequestParam("isShow") boolean isShow) {
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size, new Sort(Sort.DEFAULT_DIRECTION.DESC, "createDate"));
        Page<UserInfo> byConsition = userService.findByConsition(pageable, searchKey, beginDate, endDate, isShow);
        return byConsition;
    }


    @PostMapping("/updateStudent")
    @ApiOperation(value = "更新学生信息", notes = "返回成功或者失败")
    public String updateStudent(@RequestBody Student student) {
        Student student1 = studentService.findById(student.getId()).map(x -> {
            x.getCourseList().clear();
            x.getCourseList().addAll(student.getCourseList());
            x.setAge(student.getAge());
            x.setName(student.getName());
            return x;

        }).orElse(null);
        if (Objects.nonNull(student1)) {
            studentService.add(student1);
        }
        return "200OK";
    }


    @PostMapping("/addStudent")
    @ApiOperation(value = "新增学生信息", notes = "返回成功或者失败")
    public String addStudent(@RequestBody Student student) {
        studentService.add(student);
        return "200OK";
    }


}
