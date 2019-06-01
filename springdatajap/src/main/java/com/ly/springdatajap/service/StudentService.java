package com.ly.springdatajap.service;

import com.ly.springdatajap.entity.Student;
import com.ly.springdatajap.resposity.StudentResposity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentResposity studentResposity;


    public boolean update(Student student){
         studentResposity.save(student);
         return true;
    }

    public boolean add(Student student){
        studentResposity.save(student);
        return true;
    }


    public Optional<Student> findById(Integer id){
        return  studentResposity.findById(id);
    }



}
