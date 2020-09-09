package com.odianyun.search.whale.data.model.hotword;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author hs
 * @date 2018/9/14.
 */
@Data
@NoArgsConstructor
public class SearchWordFrequencyVO {
    private Long id;

    private String keyword;

    private Integer frequency;

    private String date;

    private Integer preFrequency;

    private Double coolingCoefficient;

    private Integer tomorrowFrequency;

    private Integer rank;

    private Integer wordStatus;

    private String frontKeyword;

    private String ex1;

    private String ex2;

    private String ex3;

    private Integer isAvailable;

    private Integer isDeleted;

    private Integer versionNo;

    private Long createUserid;

    private String createUsername;

    private String createUserip;

    private String createUsermac;

    private Date updateTimeDb;

    private Date updateTime;

    private Date createTime;

    private Date createTimeDb;

    private String serverIp;

    private Long updateUserid;

    private String updateUsername;

    private String updateUserip;

    private String updateUsermac;

    private Long companyId;

    private String clientVersionno;

    private Integer limit;
    private Integer offset;
}
