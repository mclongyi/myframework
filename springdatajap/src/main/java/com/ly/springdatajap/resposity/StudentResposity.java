package com.ly.springdatajap.resposity;

import com.ly.springdatajap.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentResposity extends JpaRepository<Student, Integer> {


}
