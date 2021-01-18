package com.longyi.csjl.tools;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class Country implements Serializable {
    @ApiModelProperty(value="")
    private String code;

    @ApiModelProperty(value="")
    private String name;


    @ApiModelProperty(value="")
    private Object continent;

    @ApiModelProperty(value="")
    private String region;

    @ApiModelProperty(value="")
    private Double surfacearea;

    @ApiModelProperty(value="")
    private Short indepyear;

    @ApiModelProperty(value="")
    private Integer population;


    @ApiModelProperty(value="")
    private Double lifeexpectancy;


    @ApiModelProperty(value="")
    private Double gnp;

    @ApiModelProperty(value="")
    private Double gnpold;

    @ApiModelProperty(value="")
    private String localname;


    @ApiModelProperty(value="")
    private String governmentform;

    @ApiModelProperty(value="")
    private String headofstate;


    @ApiModelProperty(value="")
    private Integer capital;

    @ApiModelProperty(value="")
    private String code2;


}