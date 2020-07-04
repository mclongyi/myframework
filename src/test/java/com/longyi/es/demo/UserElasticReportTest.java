package com.longyi.es.demo;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserElasticReportTest {

    @Autowired
    private UserElasticReport userEleasticRespority;

    @Test
    void save() {
        Student student=new Student();
        student.setAge(20);
        student.setDesc("最强DJ");
        student.setId(UUID.randomUUID().toString().replace("-",""));
        student.setName("mclongyi");
        userEleasticRespority.save(student);
    }

    @Test
    void findById() {
    }

    @Test
    void findAll() {
    }
}