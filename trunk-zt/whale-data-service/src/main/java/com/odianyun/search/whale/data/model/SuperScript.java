package com.odianyun.search.whale.data.model;

import java.util.Date;

/**
 * Created by admin on 2016/12/1.
 */
public class SuperScript {
    private Long superscriptId;//角标id
    private String name;
    private Integer displayType;
    private String iconUrl;
    //private Long mp_num;
    private Long priority;//优先级
    private Date createTime;

    public Long getSuperscriptId() {
        return superscriptId;
    }

    public void setSuperscriptId(Long superscriptId) {
        this.superscriptId = superscriptId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDisplayType() {
        return displayType;
    }

    public void setDisplayType(Integer displayType) {
        this.displayType = displayType;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
