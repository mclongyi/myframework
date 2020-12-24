package com.longyi.csjl.elasticsearch;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Data
@Document(indexName = "studentindex",type = "fs")
@ToString
public class Student {

    @Id
    @Field(type=FieldType.Keyword)
    private String id;

    @MultiField(mainField=@Field(type=FieldType.Text),
            otherFields= {@InnerField(suffix="keyWord",type=FieldType.Keyword)})
    private String name;

    @Field(type = FieldType.Integer)
    private Integer age;

    @MultiField(mainField=@Field(type=FieldType.Text),
            otherFields= {@InnerField(suffix="keyWord",type=FieldType.Keyword)})
    private String desc;
}
