package com.ly.springdatajap.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="student")
@Data
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Integer id;

    @Column(name = "name")
    private String name;

    @Column(name="age")
    private Integer age;


    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER,orphanRemoval = true)
    @JoinColumn(name = "student_course_id",referencedColumnName = "id")
    private List<Course> courseList;

}
