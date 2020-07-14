package com.longyi.csjl.webservice.dto;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author ly
 * @Description TODO
 * @date 2020/7/14 16:13
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "ZT_STUDENT_INFO.response")
@Data
public class StudentDTO {

    @XmlElement(name = "ZT_AGE")
    private Integer age;

    @XmlElement(name = "ZT_WEIGHT")
    private Double weight;


    @XmlElement(name = "ZT_NAME")
    private String name;

    @XmlElement(name = "ZT_SCHOOL_NAME")
    private String schoolName;

    @XmlElement(name = "ZT_CLASS_NAME")
    private String className;

    @XmlElement(name = "ZT_ITEM")
    private List<SubjectDTO> list;






}    
   