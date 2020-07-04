package com.longyi.es.demo;


import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "studentindex",type = "student")
public class Student {

    private String id;
    @Field(analyzer = "ik_smart",type = FieldType.Text)
    private String name;
    private Integer age;
    @Field(analyzer = "ik_smart",type = FieldType.Text)
    private String desc;

}
