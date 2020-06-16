package com.ly.springdatajap.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "course")
@Data
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;


    @Column(name = "course_name")
    private String courseName;

    @Column(name = "score")
    private Integer score;

    @ManyToOne
    @JoinColumn(name = "student_course_id", referencedColumnName = "id")
    private Student student;

}
