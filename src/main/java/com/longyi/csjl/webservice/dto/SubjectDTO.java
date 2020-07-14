package com.longyi.csjl.webservice.dto;

import lombok.Data;

import javax.xml.bind.annotation.*;

/**
 * @author ly
 * @Description TODO
 * @date 2020/7/14 16:14
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "ZT.SUBJECT")
@Data
public class SubjectDTO {

    @XmlElement(name = "ZT_SUBJECT_NAME")
    private String subjectName;

    @XmlElement(name = "ZT_SORCE")
    private Double sorce;

    @XmlElement(name = "ZT_SUBJECT_TEACHER")
    private String subjectTeacher;

}    
   