package com.longyi.csjl.webservice.dto;

import lombok.Data;

import javax.xml.bind.annotation.*;

/**
 * @author ly
 * @Description TODO
 * @date 2020/7/14 16:59
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
})
@XmlRootElement(name = "ZT_PARAM.request")
public class ZT_PARAM {

    @XmlElement(name = "ZT_NAME")
    private String studentName;

    @XmlElement(name = "ZT_AGE")
    private Integer age;



}    
   